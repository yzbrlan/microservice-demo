package SELab;

import SELab.domain.User;
import SELab.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCrypt;

@SpringBootApplication
@EnableEurekaClient
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    /**
     * This is a function to create some basic entities when the application starts.
     * Now we are using a In-Memory database, so you need it.
     * You can change it as you like.
     */
    @Bean
    public CommandLineRunner dataLoader(UserRepository userRepository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                // Create authorities if not exist.

                // Create an admin if not exists.
                if (userRepository.findByUsername("admin") == null) {
                    User admin = new User(
                            "admin",
                            "adminManager",
                            BCrypt.hashpw("Padmin", BCrypt.gensalt()),
                            "libowen@fudan.edu.cn",
                            "Fudan University",
                            "ShangHai China"
                    );
                    userRepository.save(admin);
                }

                if (userRepository.findByUsername("wuxiya") == null) {
                    User admin = new User(
                            "wuxiya",
                            "wuxiyaManager",
                            BCrypt.hashpw("Pwuxiya", BCrypt.gensalt()),
                            "wuxiya@fudan.edu.cn",
                            "Fudan University",
                            "ShangHai China"
                    );
                    userRepository.save(admin);
                }

                if (userRepository.findByUsername("zhangyimeng") == null) {
                    User admin = new User(
                            "zhangyimeng",
                            "zhangyimengManager",
                            BCrypt.hashpw("Pzhangyimeng", BCrypt.gensalt()),
                            "zhangyimeng@fudan.edu.cn",
                            "Fudan University",
                            "ShangHai China"
                    );
                    userRepository.save(admin);
                }

                if (userRepository.findByUsername("test") == null) {
                    User admin = new User(
                            "test",
                            "testManager",
                            BCrypt.hashpw("123456", BCrypt.gensalt()),
                            "test@fudan.edu.cn",
                            "Fudan University",
                            "ShangHai China"
                    );
                    userRepository.save(admin);
                }

                if (userRepository.findByUsername("test1") == null) {
                    User admin = new User(
                            "test1",
                            "test1Manager",
                            BCrypt.hashpw("123456", BCrypt.gensalt()),
                            "test@fudan.edu.cn",
                            "Fudan University",
                            "ShangHai China"
                    );
                    userRepository.save(admin);
                }
                if (userRepository.findByUsername("test2") == null) {
                    User admin = new User(
                            "test2",
                            "test2Manager",
                            BCrypt.hashpw("123456", BCrypt.gensalt()),
                            "test@fudan.edu.cn",
                            "Fudan University",
                            "ShangHai China"
                    );
                    userRepository.save(admin);
                }
                if (userRepository.findByUsername("test3") == null) {
                    User admin = new User(
                            "test3",
                            "test3Manager",
                            BCrypt.hashpw("123456", BCrypt.gensalt()),
                            "test@fudan.edu.cn",
                            "Fudan University",
                            "ShangHai China"
                    );
                    userRepository.save(admin);
                }
            }


        };
    }
}
