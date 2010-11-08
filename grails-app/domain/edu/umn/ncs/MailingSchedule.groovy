package edu.umn.ncs

class MailingSchedule {

    Instrument instrument
    Date checkpointDate
    Integer quota

    static constraints = {
        instrument()
        checkpointDate(unique:'instrument')
        quota()
    }

    String toString(){
        "Quota: ${quota} on ${checkpointDate}"
    }
}
