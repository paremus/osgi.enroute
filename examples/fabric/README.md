# Paremus Service Fabric

To run the enRoute examples on the Paremus Service Fabric, we need to create a System document that references index(es) containing all dependencies.

The microservice-index project creates a single index by combining the existing enRoute indexes and adding the example bundles.

The microservice-jdbc-system project generates the System document using the template in `microservice-jdbc-system/src/main/paremus/system.xml` and the BND run file `microservice-jdbc-system/src/main/resources/microservice-jdbc.bndrun`

These artifacts then need to be deployed to a repository where they can be imported by the Paremus Service Fabric.

The following command:

```
mvn -s settings.xml -P CI_Build clean deploy
```

rebuilds the artifacts with remote URLs and deploys to the Paremus Nexus repository.

If you want to try this yourself, you need to use a different repository to which you have write access.
