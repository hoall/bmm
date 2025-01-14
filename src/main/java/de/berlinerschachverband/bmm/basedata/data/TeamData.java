package de.berlinerschachverband.bmm.basedata.data;

import java.util.Optional;

public record TeamData(Long id,
                       ClubData clubData,
                       Optional<DivisionData> division,
                       Integer number) {

    public String name() {
        return clubData().name()+" "+number;
    }
}
