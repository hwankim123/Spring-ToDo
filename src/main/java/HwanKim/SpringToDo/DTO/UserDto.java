package HwanKim.SpringToDo.DTO;

import HwanKim.SpringToDo.domain.Role;
import HwanKim.SpringToDo.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserDto {

    private Long id;
    private String name;
    private String email;
    private Role role;

    public UserDto(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole();
    }

    public User toEntity(){
        return User.builder()
                .name(name)
                .email(email)
                .role(Role.USER)
                .build();
    }
}
