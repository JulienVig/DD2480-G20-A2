# Continuous Integration Server

##### Implements a CI service through a webhook Github.

The CI watches the branch `assessment` of the repository [DD2480-G20-A2](https://github.com/JulienVig/DD2480-G20-A2.git). A Github webhook is set such that the handler method of the server is called every time a `push` occurs. Therefore, the server builds the branch `assessment` every `push` event on the repository (i.e. in any branch). In other words, the server compiles the branch and runs the unit tests in the repository. We can also set the CI server to build the branch `assess_error` in order to provoke a build failure.

## Build

Compile and test the different files with
```console
foo@bar:~$ ./gradlew build
```

## Travis

Travis is configured to compile the project and perform automated tested every `push` and `pull request` events. The build history is accessible [here](https://travis-ci.org/JulienVig/DD2480-G20-A2).


## Compilation and Testing Implementation

In order to compile and test the `assessment` branch, the server clones the repository and checkout in the relevant branch using the library `JGit`. The class `GitUtil` references useful methods relevant to Git operations. Once the repository is cloned the server builds it with the `Gradle tooling API`, with which a java program can trigger `Gradle` tasks like build. Therefore, the server compiles and tests the branch through a single command. The output of the build is stored as a boolean value.

## Notification Implementation

Notification is implemented by using the open GitHub API. When the CI server recieves a push webhook the sha of the latest commit is extracted from the request body. [The API request used is documented here.](https://developer.github.com/v3/repos/statuses/#create-a-status). After building we send the status of the build to the corresponding commit on github. This is done in a method called `SetStatus` in the `GitUtil` class. This method takes a sha and a boolean representing if a build was successful or not. It then updates the corresponding commit on github by performing a `POST` request to the API.

Testing is done by performing a `GET` request to the same API. This gives us the statuses of the commit which is parsed to get the status of the latest one. The tests verify that the commit status has been correctly updated by performing two GET request before and after calling the `SetStatus` method.

## Statement of contribution

|Emil|Filip |
|:--|:--|
| Core feature #3: <br/> Notification with  <br/> `commit status`  <br/> | Architecture  of <br/> the build history |
|**Julien** | **Susie** |
Core feature #1, #2 <br/> Config Travis | Log management <br/> of the build history
