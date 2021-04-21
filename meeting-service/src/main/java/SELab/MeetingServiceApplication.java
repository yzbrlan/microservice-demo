package SELab;

import SELab.domain.Meeting;
import SELab.repository.MeetingRepository;
import SELab.utility.contract.MeetingStatus;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
public class MeetingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeetingServiceApplication.class, args);
    }
    /**
     * This is a function to create some basic entities when the application starts.
     * Now we are using a In-Memory database, so you need it.
     * You can change it as you like.
     */
    @Bean
    public CommandLineRunner dataLoader(MeetingRepository meetingRepository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                // Create authorities if not exist.

                // Create an admin if not exists.
                if (meetingRepository.findByChairName("zhangyimeng") == null||meetingRepository.findByChairName("zhangyimeng").size()==0) {
                    Meeting meeting=new Meeting();
                    meeting.setChairName("zhangyimeng");
                    meeting.setAcronym("zhang");
                    meeting.setCity("ShangHai");
                    meeting.setRegion("ShangHai China");
                    meeting.setMeetingName("Mzhangyimeng");
                    meeting.setStatus(MeetingStatus.unprocessed);
                    meetingRepository.save(meeting);
                }

                // Create an admin if not exists.
                if (meetingRepository.findByChairName("wuxiya") == null||meetingRepository.findByChairName("wuxiya").size()==0) {
                    Meeting meeting=new Meeting();
                    meeting.setChairName("wuxiya");
                    meeting.setOrganizer("Tongji");
                    meeting.setCity("Sichuan");
                    meeting.setAcronym("wu");
                    meeting.setRegion("Sichuan China");
                    meeting.setMeetingName("Mwuxiya");
                    meeting.setStatus(MeetingStatus.unprocessed);
                    meetingRepository.save(meeting);
                }

                if (meetingRepository.findByChairName("test") == null||meetingRepository.findByChairName("test").size()==0) {
                    Meeting meeting=new Meeting();
                    meeting.setChairName("test");
                    meeting.setOrganizer("Tester");
                    meeting.setCity("Shanghai");
                    meeting.setAcronym("test");
                    meeting.setRegion("Shanghai China");
                    meeting.setMeetingName("MeetingTest");
                    meeting.setStatus(MeetingStatus.unprocessed);
                    meetingRepository.save(meeting);
                }
            }
        };
    }
}
