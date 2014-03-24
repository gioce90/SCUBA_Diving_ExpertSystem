(deftemplate UI-state
   (slot id (default-dynamic (gensym*)))
   (slot display) 
   (slot relation-asserted (default none)) 
   (slot type-answer         ; I have added this for choose different type of comunication
       (allowed-symbols radio check message other)
       (default radio))
   (multislot valid-answers)
   (multislot response (default none)) ; I changed this from "slot" to "multislot" 
                                       ; to handle multiple responses (typical case checkbox)
   (slot state (default middle))
)

 
(deftemplate state-list
   (slot current)                   ; stato corrente
   (multislot sequence))            ; sequenza di stati
  
(deffacts startup
   (state-list))

(defrule system-banner "Welcome Message"
  =>
  (assert (UI-state (display WelcomeMessage)
                    (relation-asserted start)
                    (valid-answers)
                    (state initial)))

;;;;;;;;;;;;;;;;;;
;; Example Rules:
;;;;;;;;;;;;;;;;;;

(defrule first-state-1 ""
   (logical (start))
   =>
   (assert (UI-state (display First1Question)
                     (relation-asserted second)
                     (response Yes)
                     (valid-answers No Yes)))
)

(defrule first-state-2 ""
   (logical (start))
   =>
   (assert (UI-state (display First2Question)
                     (relation-asserted second)
                     ;(response $?)
                     (type-answer check)
                     (valid-answers checkbox1 checkbox2 checkbox3)))
)

(defrule second-state ""
   (logical (second))
   =>
   (assert (UI-state (display SecondAndFinalQuestion)
                     (state final)))
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;;*************************
;;;* GUI INTERACTION RULES *
;;;*************************

;;; Edit by Gioacchino Piazzolla

(defrule ask-question
   (declare (salience 5))
   (UI-state (id ?id))
   ?f <- (state-list (sequence $?s&:(not (member$ ?id ?s))))
   =>
   (modify ?f (current ?id)
              (sequence ?id ?s))
   (halt)
)

(defrule handle-next-no-change-none-middle-of-chain
   (declare (salience 10))
   
   ?f1 <- (next ?id)
   ?f2 <- (state-list (current ?id) (sequence $? ?nid  ?id $?))
   =>
      
   (retract ?f1)
   
   (modify ?f2 (current ?nid))
   
   (halt)
)

(defrule handle-next-response-none-end-of-chain

   (declare (salience 10))
   
   ?f <- (next ?id)

   (state-list (sequence ?id $?))
   
   (UI-state (id ?id)
             (relation-asserted ?relation))
                   
   =>
      
   (retract ?f)

   (assert (add-response ?id))
)

(defrule handle-next-no-change-middle-of-chain

   (declare (salience 10))
   
   ?f1 <- (next ?id $?response)  ; I have added $ a ?response (keep it)

   ?f2 <- (state-list (current ?id) (sequence $? ?nid ?id $?))
     
   (UI-state (id ?id) (response $?response))  ; I have added $ a ?response (keep it)
   
   =>
      
   (retract ?f1)
   
   (modify ?f2 (current ?nid))

   (halt)
)

(defrule handle-next-change-middle-of-chain
   (declare (salience 10))
   (next ?id $?response)                        ; I have added $ a ?response (keep it)
   ?f1 <- (state-list (current ?id) (sequence ?nid $?b ?id $?e))
   (UI-state (id ?id) (response ~$?response))   ; I have added $ a ?response (keep it)
   ?f2 <- (UI-state (id ?nid))
   =>    
   (modify ?f1 (sequence ?b ?id ?e))
   (retract ?f2)
)

(defrule handle-next-response-end-of-chain
   (declare (salience 10))
   ?f1 <- (next ?id $?response)             ; I have added $ a ?response
   (state-list (sequence ?id $?))
   ?f2 <- (UI-state (id ?id)
                    (response $?expected)   ; I have added $ a ?expected (keep it)
                    (relation-asserted ?relation))  
   =>
   (retract ?f1)
   (if (neq $?response $?expected)          ; I have added $ a ?response and ?expected
      then
      (modify ?f2 (response $?response)))   ; I have added $ a ?response 
   (assert (add-response ?id  $?response))   ; I have added $ a ?response (keep it)
)

(defrule handle-add-response
   (declare (salience 10))
   (logical (UI-state (id ?id)
                      (relation-asserted ?relation)))
   ?f1 <- (add-response ?id $?response)     ; I have added $ a ?response (keep it)
   =>
   (str-assert (str-cat "(" ?relation " " (str-implode ?response) ")")) ;;;; I have added (str-implode ?response)
   (retract ?f1)

)

(defrule handle-add-response-none
   (declare (salience 10))   
   (logical (UI-state (id ?id)
                      (relation-asserted ?relation)))
   ?f1 <- (add-response ?id)
   =>
   (str-assert (str-cat "(" ?relation ")"))
   (retract ?f1)
) 


(defrule handle-prev
   (declare (salience 10))
   ?f1 <- (prev ?id)
   ?f2 <- (state-list (sequence $?b ?id ?p $?e))
   =>
   (retract ?f1)
   (modify ?f2 (current ?p))
   (halt)
)
   
