# Full-Stack-User-Inventory

- Created customer entity and coded to perform CRUD operations on customer entity using N-Tier Architecture.
- Created three DAO implementations - fake(static data), JPA and JDBC hosted on postgres db engine.
- Wrote unit tests for dao layer, service layer and integration tests for controller layer.
- Packaged the JAR file and built docker image using jib-maven-plugin.
- Ran container from docker image on local and tested to verify working fine.
- Pushed image to docker hub and hosted backend api on amazon web services using aws-elastic beanstalk through dockerrun.aws.json configuration file.
- Automated the Continuous Integration using github-actions workflow to verify checks of passing unit and integration test-run on pull request.
- Automated Continuous Deployment using github-actions workflow to package application into JAR - to build docker image using jib - to deploy this image to elastic beanstalk using dockerrun.aws.json file - to have new version of hosted application.
- Integrated Slack to send customised workflow messages to our slack app through incoming webhooks.
- Created react-frontend using react-vite tool to add Sidebar and cards for customer profile-data to perform crud operations.
- Built docker image for frontend using Dockerfile, tested to work fine locally and hosted on aws domain url.
