
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Startup
@Singleton
public class AlarmBean {

    private String status;

    
  @PostConstruct
  void init (){
    status = "Ready";
    }
}
