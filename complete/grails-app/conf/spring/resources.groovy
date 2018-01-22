import demo.TagGenerationListener

// Place your Spring DSL code here
beans = {

    tagGenerationListener(TagGenerationListener, ref("hibernateDatastore"))
}
