# Gherkin Editor
## Description
Gherkin Editor is web based tool for writing acceptance criteria in gherkin format. 
The main advantage over writing the files in an developent editor:
- write the files collaborative
- easy to use, even for user without technical knowledge
- assistance while editing: the app suugests you terms you used before
- easy to integrate into your project management tool as all elements have deep links
- easy to integrate into your continuous integration setup as you can download a zip containing the files with a simple web request

## Deployment
Gherkin Editor is build on top of spring boot. Your have several deployment opions:
- Start spring boot jar via java
- Start via docker
- Start via docker-compose
- Start via Kubernetes 
For detailed instructions and examples see the [deployment page](deployment/README.md).

## Integration into other tools
### Project management e.g. Jira
All elements have deeplinks. With that in mind you can simply add the link to your ticket. You can place the link in the description or in a custom field.

### Testing and continuous integration
Via the gherkin editor rest interface you can simply download a zip of your project with all the folders and feature files. See the [Export Project Rest API](https://htmlpreview.github.io/?https://github.com/KobeTwo/gherkin-editor/blob/master/swagger/index.html#exportProjectUsingGET).

### How to deal with existing feature files
You can import a zip file containing all your folders & feature files within the app or via the [Import Projekct Rest Interface](https://htmlpreview.github.io/?https://github.com/KobeTwo/gherkin-editor/blob/master/swagger/index.html#importProjectUsingPOST)

### Other integrations
Gherkin editor offers an open web interface which is used by the vue.js frontend. You can use this interface to integrate with other systems that need to receive data from Gherkin Editor or need to write data to it in an automatic way.
For a full list of all interfaces see the swagger api documentation. You can find a [static documentation in this repo](https://htmlpreview.github.io/?https://github.com/KobeTwo/gherkin-editor/blob/master/swagger/index.html) or after the start of the gherkin editor app via the relative URL /swagger-ui.html e.g. http://localhost:8080/swagger-ui.html.
