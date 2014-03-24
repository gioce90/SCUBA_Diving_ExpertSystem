;;;======================================================
;;;  SCUBA Diving Expert System
;;;
;;;    This expert system will guide you in
;;;    planning scuba diving.
;;;
;;;    CLIPS Version 6.3
;;;    For use with the CLIPS 6.3 and JAVA GUI
;;;
;;;    Created by Gioacchino Piazzolla
;;;======================================================


;;; ***************************
;;; * DEFTEMPLATES & DEFFACTS *
;;; ***************************

(deftemplate subacqueo
    (multislot nome             ; nome sub
        (type SYMBOL))
    (slot data-nascita          ; data di nascita
        (type SYMBOL))
    (slot numero-immersioni     ; numero di immersioni
        (type INTEGER))
    (slot max-deep              ; profondita' massima raggiunta
        (type FLOAT))
    (slot brevetto              ; brevetto
        (type SYMBOL)
        (allowed-symbols
            no
            open-water
            advanced
            master)
        (default no))
    (multislot specialty        ; specialita' acquisite
        (type SYMBOL)
        (allowed-symbols
            nothing                  ; nessuna specialita'
            boat                     ;"Immersione dalla barca"
            deep                     ;"Immersione profonda"
            photography              ;"Fotografia subacquea"
            wreck                    ;"Immersione su relitto"
            navigation               ;"Navigazione"
            perfect-buoyancy         ;"Assetto perfetto"
            nitrox                   ;"Nitrox"
            dry-suit                 ;"Immersione con muta stagna"
            stress-rescue            ;"Stress&Rescue"
            equipment                ;"Manutenzione Attrezzatura"
            night-limited-visibility ;"Notturna e visibilita' limitata"
            sidemount                ;"Recreational Simemount Diving"
            altitude                 ;"Immersione in quota"
            river                    ;"Immersione nei fiumi"
            science                  ;"Science of Diving"
            search-recovery          ;"Ricerca&Recupero"
            shark                    ;"Immersione con gli Squali"
            currents)                ;"Onde, maree e correnti"
        (default nothing))
)

(deftemplate UI-state
   (slot id (default-dynamic (gensym*)))     ; id della schermata, generato automaticamente
   (slot display)                            ; messaggio della schermata
   (slot relation-asserted (default none))   ; la relazione che verrà asserita rispondendo alla quest della domanda 
   (slot type-answer                        ; tipo di risposta
       (allowed-symbols radio check message birthday)
       (default radio))
   (multislot valid-answers)                 ; risposte ammesse
   (multislot response (default none))       ; responso scelto dall'utente
   (slot state (default middle))             ; initial, middle, diver-definition, final
)

(deftemplate state-list
   (slot current)                   ; stato corrente
   (multislot sequence))            ; sequenza di stati
  
(deffacts startup
   (state-list)    
   (subacqueo))


;;;****************
;;;* STARTUP RULE *
;;;****************

(defrule system-banner "Welcome Message"
  =>
  (assert (UI-state (display WelcomeMessage)
                    (relation-asserted start)
                    (valid-answers)
                    (state initial)))

;   (assert(fattuccio A (random 0 3)))
;   (assert(fattuccio B (random 0 3)))
;   (assert(fattuccio C (random 0 3)))
;   (assert(fattuccio D (random 0 3)))
)


; RANDOM RULE:
;(defrule define-initial-start ""
;  (logical (start))
;  =>
;)

(defrule add-subacqueo-ifNotExist
   (not(subacqueo))
=>
   (assert(subacqueo))
)

;;; ***************************
;;; * RULES                   *
;;; ***************************

;;; *********************
;;; * Define diver Rule *
;;; *********************

(defrule determine-immersed-state "Determina se l'utente si è mai immerso"
   ;(declare(salience 100))
   (logical (start))
   (not(ever-immersed ?))
   =>
   (assert (UI-state (display EverImmersedQuestion)
                     (relation-asserted ever-immersed)
                     (response Yes)
                     (valid-answers No Yes)))
)

(defrule determine-age-diver "Determina l'eta' dell'utente"
   ;(declare(salience 100))
   (logical (start))
   (not(age-diver ?))
  =>
   (assert (UI-state (display AgeQuestion)
                     (relation-asserted age-diver)
                     (type-answer birthday)))
)

(defrule determine-licence-state "Determina se l'utente è dotato di brevetto"
   (logical (ever-immersed Yes))
   (not(have-licence ?))
   =>
   (assert (UI-state (display LicenceQuestion)
                     (relation-asserted have-licence)
                     (response No)
                     (valid-answers No Yes)))
)

(defrule determine-level-state "Determina il livello del brevetto"
   (logical (have-licence Yes))
   (not(what-level ?))
   =>
   (assert (UI-state (display LevelQuestion)
                     (relation-asserted what-level)
                     (response Open)
                     (valid-answers Open Advanced Master)))
)

(defrule determine-specialty-state "Se brevettato, determina le specialità"
   (logical (have-licence Yes)
            (what-level ?))
   (not(what-specialty $?))
   =>
   (assert (UI-state (display SpecialtyQuestion)
                     (relation-asserted what-specialty)
                     (response nothing)
                     (type-answer check)
                     (valid-answers Boat Deep Photography Wreck Navigation
                        Perfect-buoyancy Nitrox Dry-suit Stress-rescue
                        Equipment Night-limited-visibility Sidemount Altitude
                        River Science Search-recovery Shark Currents)))
)

(defrule determine-how-deep-state "Determina la massima profondità raggiunta dall'utente"
   (or (logical (ever-immersed Yes)(have-licence No))
       (logical (ever-immersed Yes)(have-licence Yes)(what-specialty $?))) ; $? perché posso anche non avere specialita'
   (not(how-deep ?))
   =>
   (assert (UI-state (display MaxDeepQuestion)
                     (relation-asserted how-deep)
                     (response 6.0)
                     (valid-answers 6.0 7.5 9.0 10.5 12.0 15.0 18.0 21.0
                                    24.0 27.0 30.0 33.0 36.0 39.0 40.0)))
)

(defrule determine-how-many-dives-state "Determina il numero di immersioni fatte"
   (logical (ever-immersed Yes)(have-licence ?)(how-deep ?))
   (not(how-many-dives ?))
   =>
   (assert (UI-state (display HowManyDivesQuestion)
                     (relation-asserted how-many-dives)
                     (response da-1-a-12)
                     (valid-answers da-1-a-12 da-12 da-24 da-50 da-100
                            da-200 da-300 da-400 da-500 da-1000)))
)


(defrule define-diver-NoExperience ""
   (declare (salience -10))
   (logical (UI-state (id ?id))) ; state def-sub?
   (state-list (current ?id))

   ?sub <- (subacqueo)
   (age-diver ?age)
   (ever-immersed No)
   
   =>
   (modify ?sub(data-nascita ?age))

    (assert (UI-state (display DefinitionDiver)
                     (valid-answers "Esordiente")
                     (type-answer message)
                     (state diver-definition)))
)

(defrule define-diver-WithExperience-NoLicence ""
   (declare (salience -10))
   (logical (UI-state (id ?id))) ; state def-sub?
   (state-list (current ?id))

   ?sub <- (subacqueo)

   (age-diver ?age)
   (ever-immersed Yes)
   (how-deep ?hd)(how-many-dives ?hmd)
   (have-licence No)
   =>

   (modify ?sub(data-nascita ?age)(max-deep ?hd)(numero-immersioni ?hmd))

   (assert (UI-state (display DefinitionDiver)
                     (valid-answers "Sub senza brevetto")
                     (type-answer message)
                     (state diver-definition)))
)

(defrule define-diver-WithExperience-WithLicence ""
   (declare (salience -10))
   (logical (UI-state (id ?id))) ; state def-sub?
   (state-list (current ?id))

   ?sub <- (subacqueo)

   (age-diver ?age)
   (ever-immersed Yes)(how-deep ?hd)(how-many-dives ?hmd)
   (have-licence Yes)(what-level ?wl)(what-specialty $?ws)
   =>

   (modify ?sub(data-nascita ?age)
               (max-deep ?hd)(numero-immersioni ?hmd)
               (brevetto ?wl)(specialty $?ws))  ;;; !

   (assert (UI-state (display DefinitionDiver)
                     (valid-answers "Sub con brevetto")
                     (type-answer message)
                     (state diver-definition)))
)

(defrule undefine-diver ""
   (declare (salience -10))
   (logical (UI-state (id ?id))) ; state def-sub?
   (state-list (current ?id))
   =>
    (assert (UI-state (display DefinitionDiver)
                     (valid-answers "Non sono riuscito a definire la tua esperienza")
                     (type-answer message)
                     (state diver-definition)))
)








;;;*************************
;;;* GUI INTERACTION RULES *
;;;*************************

;;; Modifiche aggiunte da Gioacchino Piazzolla
;;; - Ho trasformato tutte le variabili ?response in $?response
;;; - Ho trasformato tutte le variabili ?expected in $?expected
;;; - Ho cambiato handle-add-response in modo che gestisse i multifield

; lavora solo sulla state-list. È corretta. 
(defrule ask-question
   (declare (salience 5))
   (UI-state (id ?id))
   ?f <- (state-list (sequence $?s&:(not (member$ ?id ?s))))
   =>
   (modify ?f (current ?id)
              (sequence ?id ?s))
   (halt)
)

; regola che si attiva quando, ritornando su una scelta fatta, non 
; si cambia nulla e si riconferma lo stesso risultato
; Però si attiva quando ho deselezionato tutte le checkboxes, e non dovrebbe farlo
(defrule handle-next-no-change-none-middle-of-chain
   (declare (salience 10))
   
   ?f1 <- (next ?id)
   ?f2 <- (state-list (current ?id) (sequence $? ?nid  ?id $?))
   =>
      
   (retract ?f1)
   
   (modify ?f2 (current ?nid))
   
   (assert(__FATTO__ ?id in handle-next-no-change-none-middle-of-chain __WHIT_NO_RESPONSE__ ?nid))

   (halt)
)


; handle-next-response-none-end-of-chain
; si occupa dei responsi nulli, cioè quando non c'è una scelta da
; effettuare (ad esempio il Messaggio di benvenuto)
; teoricamente in seguito dovrebbe attivarsi la regola handle-add-response-none,
; ma da quando sono passato ai multifield si attiva sempre 
; handle-add-response, con response vuoto 
(defrule handle-next-response-none-end-of-chain

   (declare (salience 10))
   
   ?f <- (next ?id)

   (state-list (sequence ?id $?))
   
   (UI-state (id ?id)
             (relation-asserted ?relation))
                   
   =>
      
   (retract ?f)

   (assert (add-response ?id))

   (assert(__FATTO__ ?id in handle-next-response-none-end-of-chain __WHIT_NO_RESPONSE__))

)

; regola per gestire il non cambiamento nel mezzo della catena.
; Si attiva quando, fatta una scelta, torno indietro ma riconfermo senza cambiare la mia scelta
(defrule handle-next-no-change-middle-of-chain

   (declare (salience 10))
   
   ?f1 <- (next ?id $?response)  ; ho aggiunto $ a response (tienilo)

   ?f2 <- (state-list (current ?id) (sequence $? ?nid ?id $?))
     
   (UI-state (id ?id) (response $?response))  ; ho aggiunto $ a response (tienilo)
   
   =>
      
   (retract ?f1)
   
   (modify ?f2 (current ?nid))
   
   (assert(__FATTO__ ?id in handle-next-no-change-middle-of-chain __WHIT_RESPONSE__ $?response))

   (halt)
)


; regola che si attiva se avviene un cambiamento di scelta da parte dell'utente
; in seguito dovrebbe attivarsi handle-next-response-end-of-chain
(defrule handle-next-change-middle-of-chain
   (declare (salience 10))
   (next ?id $?response)                        ; ho aggiunto $  a response (tienilo)
   ?f1 <- (state-list (current ?id) (sequence ?nid $?b ?id $?e))
   (UI-state (id ?id) (response ~$?response))   ; ho aggiunto $  a response (tienilo)
   ?f2 <- (UI-state (id ?nid))
   =>    
   (modify ?f1 (sequence ?b ?id ?e))
   (retract ?f2)

   (assert(__FATTO__ ?id in handle-next-change-middle-of-chain __WHIT_RESPONSE__ $?response))
)

; Si attiva quando abbiamo compiuto una scelta tra più opzioni.
; Il risultato è aggiunto alla FINE della catena.
; A volte response ed expected possono differire.
; expected è il responso scelto nella definizione di una UI-state,
; oppure è scelto dall'utente che poi è tornato indietro.
; In seguito dovrebbe attivarsi la regola handle-add-response
(defrule handle-next-response-end-of-chain
   (declare (salience 10))
   ?f1 <- (next ?id $?response)             ;;;; ho aggiunto $  a response
   (state-list (sequence ?id $?))
   ?f2 <- (UI-state (id ?id)
                    (response $?expected)   ;;;; ho aggiunto $  (tienilo)
                    (relation-asserted ?relation))  
   =>
   (retract ?f1)
   (if (neq $?response $?expected)          ;;;; ho aggiunto $  a response e a expected
      then
      (modify ?f2 (response $?response)))   ;;;; ho aggiunto $  a response
   (assert (add-response ?id  $?response))   ;;;; ho aggiunto $  a response
   
   (assert(__FATTO__ ?id in handle-next-response-end-of-chain __WHIT_RESPONSE__ $?response __AND_EXPECTED__ $?expected)) ; prova
)

(defrule handle-add-response
   (declare (salience 10))
   (logical (UI-state (id ?id)
                      (relation-asserted ?relation)))
   ?f1 <- (add-response ?id $?response)     ;;;; ho aggiunto $  a response (tienilo!)
   =>
   (str-assert (str-cat "(" ?relation " " (str-implode ?response) ")")) ;;;; ho aggiunto (str-implode $?response)
   (retract ?f1)
   (assert(__FATTO__ ?id handle-add-response __WHIT_RESPONSE__  $?response))
)

;forse è da spostare su di uno.
; da quando sono passato ai multifiel pare che questa regola non venga più usata
(defrule handle-add-response-none
   (declare (salience 10))   
   (logical (UI-state (id ?id)
                      (relation-asserted ?relation)))
   ?f1 <- (add-response ?id $?noresponse&:(eq (length$ ?noresponse) 0))
   ;?f1 <- (add-response ?id $?response&:(eq(length$ $?response) 0)) ;; ho provato a modificare qui
   =>
   (str-assert (str-cat "(" ?relation ")"))
   (retract ?f1)

   (assert(__FATTO__ ?id in handle-add-response-none))
) 


(defrule handle-prev
   (declare (salience 10))
   ?f1 <- (prev ?id)
   ?f2 <- (state-list (sequence $?b ?id ?p $?e))
   =>
   (retract ?f1)
   (modify ?f2 (current ?p))
   (halt))
   
