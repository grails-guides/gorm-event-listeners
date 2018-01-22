package demo

import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration
import org.springframework.context.ConfigurableApplicationContext

class Application extends GrailsAutoConfiguration {
    static void main(String[] args) {
        GrailsApp.run(Application, args)
    }

    @Override
    void doWithApplicationContext() {
        TagGenerationListener tagGenerationListener = applicationContext.getBean('tagGenerationListener', TagGenerationListener)

        def ctx = (ConfigurableApplicationContext) applicationContext
        ctx.addApplicationListener(tagGenerationListener)
    }
}