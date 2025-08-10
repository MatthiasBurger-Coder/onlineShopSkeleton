package de.burger.it

import spock.lang.Specification

class SanitySpec extends Specification {
    def "spock is wired"() {
        expect:
        1 + 1 == 2
    }
}
