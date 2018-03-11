import com.trick.security.DemoApplication;
import com.trick.security.user.dao.UserMapper;
import com.trick.security.user.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.UUID;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class DaoTest {

    @Autowired
    public UserMapper userMapper;

    @Test
    public void userTest() throws Exception {
       /* mvc.perform(MockMvcRequestBuilders.get("/user/register")
                .content("{username:'test',password:'password'}")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk());*/
        User user = new User();
        String id = UUID.randomUUID().toString();
        user.setId(id);
        user.setUsername("aaa");
        user.setPassword("bbb");
        LocalDate date = LocalDate.now();
        user.setBirthday(date);
        userMapper.insert(user);
    }
}
