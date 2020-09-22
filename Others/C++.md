1. 函数指针:double (*ptr)(int)
    > double为函数返回值，ptr为指向函数的指针，int为参数；
    > 必须在声明中使用括号将*ptr括起，否则double *ptr(int)表示一个返回double指针的函数ptr
    > 调用方法时为*ptr(123)，当然ptr(123)也是被C++允许的。

2. lambda表达式:
    1. 当表达式完全由一条返回语句组成时可以没有返回值，使用decltyp根据返回值推断出来，如果没有返回则为void。栗子：```[](int x){return x % 3 == 0;}```
    2. 如果表达式有多条则需要使用新增的返回类型后置语法。栗子：```[](double x) -> double{int y = x; return x - y;}```
    3. lambad可访问作用域内的任何动态变量；要捕获使用的变量，可将其名称放在中括号内。如果只指定变量名，如[z]，将按值访问变量；如果在名称前加上&，如[&z]，将按引用访问变量；如果是[&]，则能够以引用访问所有变量；如果是[=]，则是值访问；这几种方式可以混合使用，如[z, &x] or [=, &x]。

3. 重载运算符：eg：```bool operator == (const sp<T>& o)；```

4. operator()():重载了()运算符

5. static_assert：C++11引入的用于在实现编译期间的断言，static_assert（常量表达式返回布尔值，要提示的字符串），如果第一个参数常量表达式的值为false，会产生一条编译错误，错误位置就是该static_assert语句所在行，第二个参数就是错误提示字符串。；

6. 类的自动转换
    1. 只有接受一个参数的构造函数才能作为转换函数，如：Stonewt(double)函数，C++ Primer Plus 6（中文版）p413。
    2. 如果转换函数声明中使用了关键字explicit，则只用于显示强制类型转换
    3. 隐式转换的情况
        1. 将Stonewt对象初始化为double值：Stonewt s = 1.0；
        2. 将double值赋给Stonewt对象时：Stonewt s; s = 1.0;
        3. 将double值传递给接受Stonewt参数的函数时：void display(const Stonew & st); display(1.0);
        4. 返回值被声明为Stonewt的函数师徒返回double值时。

7. 常量指针&指针常量
    1. const 在*前(const int *p， int const *p)：表示const修饰的为所申明的类型。常量指针。
        - 指针所指向的内存地址所对应的值，是const，因此不可修改。但指针所指向的内存地址是可以修改的（指针可以指向其他地址）
    2. const 在*后(int* const p)：表示const修饰的为指针。指针常量。
    3. 前*后均有：表示const同时修改类型和指针。指向常量的指针常量

8. 类型转换
    1. reinterpret_cast：是C++里的强制类型转换符。操作符修改了操作数类型,但仅仅是重新解释了给出的对象的比特模型而没有进行二进制转换。
        - eg：```int *n= new int；double *d=reinterpret_cast<double*> (n);```在进行计算以后, d 包含无用值. 这是因为 reinterpret_cast 仅仅是复制 n 的比特位到 d, 没有进行必要的分析。因此, 需要谨慎使用 reinterpret_cast。并且：reinterpret_cast 只能在指针之间转换。
    2. static_cast < type-id > ( expression )，编译器隐式执行任何类型转换都用这个。
        1. 用于类层次结构中基类和子类之间指针或引用的转换。
            1. 进行上行转换（把子类的指针或引用转换成基类表示）是安全的；
            2. 进行下行转换（把基类指针或引用转换成子类表示）时，由于没有动态类型检查，所以是不安全的。
        2. 用于基本数据类型之间的转换，如把int转换成char，把int转换成enum。<font color=00CED1>这种转换的安全性也要开发人员来保证，转换错误会报错</font>。
        3. 把空指针转换成目标类型的空指针。
        4. 把任何类型的表达式转换成void类型。
    3. dynamic_cast：只用于对象的指针和引用。当用于多态类型时，它允许任意的隐式类型转换以及相反过程。不过，与static_cast不同，在后一种情况里（注：即隐式转换的相反过程），dynamic_cast会检查操作是否有效。也就是说，它会检查转换是否会返回一个被请求的有效的完整对象。检测在运行时进行。<font color=00CED1>如果被转换的指针不是一个被请求的有效完整的对象指针，返回值为NULL</font>。
    4. const_cast 一般用于强制消除对象的常量性。它是唯一能做到这一点的 C++ 风格的强制转型。
        eg:```class C {};
        const C *a = new C;
        C *b = const_cast<C *>(a);```

9. 内存对其
    1. 结构体第一个成员的偏移量（offset）为0，以后每个成员相对于结构体首地址的 offset 都是该成员大小与有效对齐值中较小那个的整数倍，如有需要编译器会在成员之间加上填充字节。
    2. 结构体的总大小为有效对齐值的整数倍，如有需要编译器会在最末一个成员之后加上填充字节。
    > 有效对齐值：#pragma pack(n)