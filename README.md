<p align="center">
  <img src="https://raw.githubusercontent.com/rwth-acis/las2peer/master/img/logo/bitmap/las2peer-logo-128x128.png" />
</p>
<h1 align="center">las2peer-CitRec-Handler-Service</h1>

![Java CI with Gradle](https://github.com/rwth-acis/las2peer-template-project/workflows/Java%20CI%20with%20Gradle/badge.svg?branch=master)
[![codecov](https://codecov.io/gh/rwth-acis/las2peer-template-project/branch/master/graph/badge.svg)](https://codecov.io/gh/rwth-acis/las2peer-template-project)
[![Dependencies](https://img.shields.io/librariesio/github/rwth-acis/las2peer-template-project)](https://libraries.io/github/rwth-acis/las2peer-template-project)

This service is a part of Citation Recommendation Bot. **Please look at  [Citation Recommendation Bot](https://github.com/rwth-acis/Citation-Recommendation-Bot) for more details.**

## Java

las2peer uses **Java 14**.


## Build

Execute the following command on your shell:

```bash
gradle clean build
```

## Start

This service needs bootstrap to the [las2peer-social-bot-manager-service](https://github.com/rwth-acis/las2peer-social-bot-manager-service).

For bootstrapping, after starting the las2peer-social-bot-manager-service, please execute command `getLocalNodeInfo` command in the terminal of the las2peer-social-bot-manager-service, then you will found an IP address with a port. Copy it and add following parameter in the start_network.bat or start_network.sh script in the line beginning with `java -cp`:

```bash
-b <ip:port>
```

To start this service, use one of the available start scripts:

Windows:

```
bin/start_network.bat
```

Unix/Mac:

```
bin/start_network.sh
```

