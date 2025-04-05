# Testing w/ Gradle

## Contents

- [Testing Gradle from CLI](#testing-gradle-from-cli)
  - [1. Running Entire Test Suite](#1-running-entire-test-suite)
  - [2. Sending Output to Console](#2-sending-output-to-console)
  - [3. Running Clean Test](#3-running-clean-test)
  - [4. Viewing Test Reports](#4-viewing-test-reports)
  - [5. Running Tests For a Specific Submodule](#5-running-tests-for-a-specific-submodule)
  - [6. Running a Specific Test Class](#6-running-a-specific-test-class)
  - [7. Running a Specific Test Method](#7-running-a-specific-test-method)


- [Testing Gradle from Intellij](#testing-gradle-from-intellij)
  - [1. Testing Entire Project](#1-testing-entire-project)
  - [2. Running Tests For Single Submodule (Project Window)](#2-running-tests-for-single-submodule--service-project-window)
  - [3. Running Tests For Single Class (Project Window)](#3-running-tests-for-single-class-file-project-window)
  - [4. Running Tests From Test Class File](#4-running-tests-from-a-test-class)

---
## Documentation
- [Readme](../README.md)
- [Building](BUILD.md)
- [Release Notes](RELEASE.md)
- [Running Services](RUNNING.md)
- [Testing Services](TESTING.md)
- [Support](SUPPORT.md)
---
## Testing Gradle from CLI

#### 1. Running Entire Test Suite

This only shows success/failure. No output is sent to the console

```shell
./gradlew test
```
---

#### 2. Sending Output to Console

This sends a lot of information to the console.

```NOTE: Using the --debug flag can leak secure information.```

```shell
./gradlew test --info
./gradlew test --debug
```

The output will be far more detailed w/ this commands. 

```text
#############################################################################
   WARNING WARNING WARNING WARNING WARNING WARNING WARNING WARNING WARNING

   Debug level logging will leak security sensitive information!

   For more details, please refer to https://docs.gradle.org/8.10/userguide/logging.html#sec:debug_security in the Gradle documentation.
#############################################################################
```
^^This is important to keep in mind when setting up your logging. 


---

#### 3. Running Clean Test

Guarantees previous test results are cleared. 
```shell
./gradlew clean test
```
---

#### 4. Viewing Test reports

Navigate to `./build/reports/tests/test/index.html`

```NOTE: The build directory will be nested in a submodule, not in the parent build directory.```

Clicking on the HTML file will show the raw HTML, but in Intellij a pop-up allows you to launch it into a browser.

![build-report-test-summary.png](img/build-report-test-summary.png)

Some test classes will allow you to select Standard Output to see the actual results

![build-report-test-summary-standard-output.png](img/build-report-test-summary-standard-output.png)
---


#### 5. Running tests for a specific submodule
Still a lot of info...

```shell
./gradlew :product-service:test --info
```
---

#### 6. Running a specific test class

```shell
./gradlew :product-composite:test --tests "org.emangini.servolution.composite.product.MessagingTests"
```
---

#### 7. Running a specific test method

```shell
./gradlew :product-composite-service:test --tests "org.emangini.servolution.composite.product.MessagingTests.createFullCompositeProduct"
```
---

---

## Testing Gradle from Intellij

## NOTE: TESTCONTAINERS

To get tests running, you must add an EV to every test profile: 

```TESTCONTAINERS_RYUK_DISABLED=true```

There is a apparently a bug in Intellij, and this variable won't be read by SYSTEM (regardless of how it is set). I've 
logged it w/ JetBrains.

### 1. Testing Entire Project

1. Create a Run/Debug Config as follows
![whole-project-rundebug.png](img/whole-project-rundebug.png)

2. Select this Configuration and click the Green Arrow to "Run Whole Project"

(Alternatively if you select-click the 4 microservices submodules and then right-click the selection you can select "Run All Tests" )
![execute-whole-project-test.png](img/execute-whole-project-test.png)

---

### 2. Running Tests For Single Submodule / Service (Project Window)

1. In the Project Window, navigate to the submodule / service you want to test

2. Right Click on the Service and select "Run Tests.."
![test-window.png](img/test-window.png)

The _right_ pane provides specific test output

The _left_ panel will give you a tree for the test execution
- Test Results
  - Test Class (there will be multiple Test Classes) 
    - Test Methods

Above the test tree, there are several notable icons
- to the far left are three circular arrows and a self-explanatory stop button
  - the first executes the tests
  - the second re-executes **failed** tests
  - the third sets the tests to re-run automatically

- There is a check mark and a circle-slash (like no-smoking) icon. 
  - When unselected, the check mark will hide **passed** tests
  - When unselected, the circle slash will hide **ignored** tests.

3. Right Clicking on any test (class or submodule) allows you to re-run just that test scope

### 3. Running Tests for Single Class File (Project Window)

1. Navigate to the Test Class you want to execute
2. Right Click and select "Run <name of test class>"

Everything else works the same as when running tests for an entire submodule, but the scope is moved down
one level of the test tree (i.e. there will only be a single test class)

3. Right Clicking on any test method in the test class will re-execute that method

### 4. Running Tests From a Test Class

1. Navigate to the test class you want to execute
2. Selecting the Green Arrows/Checkmark next to the class declaration will run all tests in the class
3. Selecting the Green Arrow/Checkmark next to the test method will execute just that method.