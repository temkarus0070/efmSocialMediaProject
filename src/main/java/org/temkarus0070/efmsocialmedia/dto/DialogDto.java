package org.temkarus0070.efmsocialmedia.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DialogDto {

    private String friendUsername;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DialogDto dialogDto = (DialogDto) o;
        return friendUsername.equals(dialogDto.friendUsername);
    }

    @Override
    public int hashCode() {
        return Objects.hash(friendUsername);
    }
}
