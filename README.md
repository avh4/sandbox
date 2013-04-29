[![Build Status](https://secure.travis-ci.org/avh4/sandbox.png?branch=master)](http://travis-ci.org/avh4/sandbox)

## Sandbox

Create and manage temporary folders to simplify integration testing

### Usage

Add sandbox as a dependency (you probably only want it for tests).  Maven dependency info:

```xml
    <dependency>
        <groupId>net.avh4.util</groupId>
        <artifactId>sandbox</artifactId>
        <version>0.0.6</version>
        <scope>test</scope>
    </dependency>
```

Any code that wants to play in the sandbox should have a root path that can be injected:

```java
    public class MyService {
        private final File root;
        
        public MyService(File root) {
            this.root = root;
        }
        
        public void doSomething() {
            File configFile = new File(root, "config.xml");
            File fileToWrite = new File(root, "output.txt");
            ...
        }
    }
```

Create a sandbox and populate it with some files.  This example copies `test-config.xml` from the classpath
into `config.xml` in the sandbox.

```java
    Sandbox sandbox = new Sandbox();
    sandbox.useResource("test-config.xml", "config.xml");
```

Test your code in the sandbox.

```java
    MyService service = new MyService(sandbox.getRoot());
    assert(sandbox.newFile("output.txt").exists() == true);
```
