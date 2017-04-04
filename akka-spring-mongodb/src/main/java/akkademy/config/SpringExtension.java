package akkademy.config;

import akka.actor.Extension;
import akka.actor.Props;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringExtension implements Extension {

    private ApplicationContext applicationContext;

    public void init(ApplicationContext applicationContext){
        this.applicationContext = applicationContext;
    }

    public Props createConfigurationForActor(String actorBeanName){
        return Props.create(ActorProducer.class, applicationContext, actorBeanName);
    }
}
