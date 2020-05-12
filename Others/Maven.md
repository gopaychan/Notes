1. 对于依赖```<dependency></dependency>```
    1. ```<optional>true</optional>``` 表示当前依赖不传递给上层，即依赖该pom的，对于pom依赖的这个依赖不可达。
    2. ```<scope>compile</scope>``` 默认为compile，参与编译。