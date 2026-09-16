package teamdevhub.devhub.medium.shared.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = teamdevhub.devhub.DevhubApplication.class)
@AutoConfigureMockMvc
class WebSecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("?몄쬆?놁씠_?묎렐?섎㈃_Unauthorized_?곹깭肄붾뱶媛_諛섑솚?쒕떎")
    void returnUnauthorizedIfAccessWithoutAuthentication() throws Exception {
        // given, when
        mockMvc.perform(get("/user/profile"))
                // then
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("adminURL_?_ADMIN_沅뚰븳???덉뼱???묎렐??媛?ν븯??")
    void allowAccessToAdminURLWithAdminRole() throws Exception {
        // given, when
        mockMvc.perform(get("/admin/users?page=0&size=10")
                        .with(user("admin@example.com").roles("ADMIN")))
                // then
                .andExpect(status().isOk());
    }

/*  媛쒕컻 ?몄쓽 ???꾩쓽 二쇱꽍 泥섎━
    @Test
    @DisplayName("adminURL_?_ADMIN_沅뚰븳???놁쑝硫??묎렐??嫄곕??쒕떎")
    void denyAccessToAdminURLWithoutAdminRole() throws Exception {
        // given, when
        mockMvc.perform(get("/admin/users?page=0&size=10")
                        .with(user("user@example.com").roles("USER")))
                // then
                .andExpect(status().isForbidden());
    }
*/

    @Test
    @DisplayName("Swagger_?묎렐?_紐⑤뱺_?ъ슜?먭?_媛?ν븯??")
    void allowAllUsersAccessToSwagger() throws Exception {
        // given, when
        mockMvc.perform(get("/swagger-ui/index.html"))
                // then
                .andExpect(status().isOk());
    }
}
