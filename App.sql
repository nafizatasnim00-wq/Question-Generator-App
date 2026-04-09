--  TOPICS TABLE
CREATE TABLE IF NOT EXISTS topics(
    topic_no INTEGER NOT NULL PRIMARY KEY,
    topic_name TEXT NOT NULL UNIQUE
);

INSERT OR IGNORE INTO topics(topic_no, topic_name) VALUES
(1, "Polymorphism"),
(2, "Inheritance"),
(3, "Encapsulation"),
(4, "Abstraction"),
(5, "Access Modifiers"), 
(6, "Static vs Instance"),
(7, "Exception Handling"), 
(8, "Interfaces & Abstract Classes");

--  QUESTIONS TABLE
CREATE TABLE IF NOT EXISTS questions(
    question_id INTEGER NOT NULL PRIMARY KEY,
    topic_id INTEGER NOT NULL,
    question_text TEXT NOT NULL,
    difficulty TEXT NOT NULL,
    FOREIGN KEY(topic_id) REFERENCES topics(topic_no)
);


-- Insert questions for each topic

INSERT OR IGNORE INTO questions VALUES
(1,1,"What is polymorphism in OOP ?","Easy"),
(2,1,"What the main two types of polymorphism?","Easy"),
(3,1,"Explain compile-time polymorphism with an example.","Medium"),
(4,1,"Explain runtime polymorphism in Java.","Medium"),
(5,1,"How does method overloading support polymorphism?","Easy"),
(6,1,"Why is polymorphism important in OOP?","Easy"),
(7,1,"Explain polymorphism using a real-life example.","Medium"),
(8,1,"What is method overriding? How does it relate to polymorphism?","Medium"),
(9,1,"Can constructors be polymorphic? Explain.","Hard"),
(10,2,"What is inheritance in OOP?","Easy"),
(11,2,"What are the advantages of inheritance?","Easy"),
(12,2,"Explain single inheritance.","Easy"),
(13,2,"Explain multilevel inheritance with an example.","Medium"),
(14,2,"Why is multiple inheritance not supported in Java classes?","Medium"),
(15,2,"What is the use of the super keyword?","Medium"),
(16,2,"Differentiate between inheritance and composition.","Hard"),
(17,3,"What is encapsulation in OOP?","Easy"),
(18,3,"How does encapsulation improve security?","Easy"),
(19,3,"Explain data hiding.","Easy"),
(20,3,"How are getters and setters related to encapsulation?","Medium"),
(21,3,"Why are class variables usually private?","Medium"),
(22,3,"Encapsulation vs abstraction—explain.","Hard"),
(23,4,"What is abstraction in OOP?","Easy"),
(24,4,"What is an abstract class?","Easy"),
(25,4,"What is an interface?","Easy"),
(26,4,"Difference between abstract class and interface.","Medium"),
(27,4,"Why can’t we create objects of an abstract class?","Medium"),
(28,4,"Explain abstraction with a real-world example.","Medium"),
(29,4,"Can an interface have method implementations? Explain.","Hard"),
(30,5,"Which access modifier offers the widest accessibility?","Easy"),
(31,5,"What is the scope of 'protected' members?","Medium"),
(32,5,"Can a top-level class be private?","Hard"),
(33,6,"What does the 'static' keyword signify in Java?","Easy"),
(34,6,"Can a static method access instance variables?","Medium"),
(35,6,"Where is static memory allocated in Java?","Hard"),
(36,7,"What is the purpose of a try-catch block?","Easy"),
(37,7,"Difference between 'throw' and 'throws'.","Medium"),
(38,7,"When does the 'finally' block execute?","Medium"),
(39,7,"What is exception handling?","Easy"),
(40,7,"What is the purpose of try-catch block?","Easy"),
(41,7,"Difference between checked and unchecked exceptions.","Hard"),
(42,7,"What is throw keyword?","Easy"),
(43,7,"What is stack unwinding?","Medium"),
(44,7,"Difference between throw and throws.","Medium"),
(45,7,"Can we use nested try blocks?","Medium"),
(46,7,"What is custom exception?","Hard"),
(47,8,"Can an interface extend multiple interfaces?","Medium"),
(48,8,"What is a marker interface?","Hard"),
(49,8,"What is the default access level for interface methods?","Easy"),
(50,8,"What is an abstract method?","Easy"),
(51,8,"Can an abstract class have constructors?","Medium"),
(52,8,"Can an interface have variables? If yes, what type?","Medium"),
(53,8,"What is the difference between implements and extends in Java?","Medium"),
(54,8,"Can a class implement multiple interfaces?","Easy"),
(55,8,"Why do we use interfaces in Java?","Easy"),
(56,8,"Can an abstract class implement an interface?","Medium"),
(57,8,"What happens if a class does not implement all methods of an interface?","Medium"),
(58,8,"Can we declare a constructor inside an interface?","Hard"),
(59,8,"What is a functional interface?","Hard");

-- OPTIONS TABLE
CREATE TABLE IF NOT EXISTS options(
    option_id INTEGER NOT NULL PRIMARY KEY,
    question_id INTEGER NOT NULL,
    option_text TEXT NOT NULL,
    is_correct INTEGER NOT NULL,
    FOREIGN KEY (question_id) REFERENCES questions(question_id)
);


  
-- Insert options for each question

INSERT OR IGNORE INTO options VALUES
-- Q1
(1,1,"Process of hiding data",0),
(2,1,"Binding data and methods together",0),
(3,1,"Ability of an object to take multiple forms",1),
(4,1,"Creating multiple classes",0),
-- Q2
(5,2,"Compile-time and Runtime polymorphism",1),
(6,2,"Static and Dynamic memory allocation",0),
(7,2,"Single and Multiple inheritance",0),
(8,2,"Abstraction and Encapsulation",0),
-- Q3
(9,3,"Method overriding is compile-time polymorphism",0),
(10,3,"Interfaces provide compile-time polymorphism",0),
(11,3,"Abstract classes use compile-time polymorphism",0),
(12,3,"Method overloading is an example of compile-time polymorphism",1),
-- Q4
(13,4,"Method overloading enables runtime polymorphism",0),
(14,4,"Method overriding enables runtime polymorphism",1),
(15,4,"Constructors provide runtime polymorphism",0),
(16,4,"Variables support runtime polymorphism",0),
-- Q5
(17,5,"Same method name with different parameter lists",1),
(18,5,"Same method name with same parameters",0),
(19,5,"Different method names with same parameters",0),
(20,5,"Same return type only",0),
-- Q6
(21,6,"It reduces memory usage",0),
(22,6,"It removes inheritance",0),
(23,6,"It increases code flexibility and reusability",1),
(24,6,"It prevents method overriding",0),
-- Q7
(25,7,"One object hiding its data",0),
(26,7,"Same person behaving differently in different roles",1),
(27,7,"Multiple classes sharing data",0),
(28,7,"Accessing private variables",0),
-- Q8
(29,8,"Same method name with different parameters",0),
(30,8,"Calling multiple constructors",0),
(31,8,"Using private methods",0),
(32,8,"Subclass redefining a superclass method",1),
-- Q9
(33,9,"No, constructors cannot be overridden",1),
(34,9,"Yes, constructors support runtime polymorphism",0),
(35,9,"Yes, constructors are inherited",0),
(36,9,"Only static constructors are polymorphic",0),
-- Q10
(37,10,"Process of hiding data",0),
(38,10,"Binding data and methods",0),
(39,10,"Mechanism to acquire properties and methods from parent class",1),
(40,10,"Creating new classes",0),
-- Q11
(41,11,"Code reusability and reduced redundancy",1),
(42,11,"Increased memory usage",0),
(43,11,"Faster execution time",0),
(44,11,"More complex code",0),
-- Q12
(45,12,"A class inherits from multiple classes",0),
(46,12,"A class inherits from one parent class",1),
(47,12,"A class is inherited by multiple classes",0),
(48,12,"Classes inherit methods from interfaces",0),
-- Q13
(49,13,"Multiple classes inherit from same parent",0),
(50,13,"A class inherits from multiple parents",0),
(51,13,"Interfaces implementing other interfaces",0),
(52,13,"Class B inherits from A, and C inherits from B",1),
-- Q14
(53,14,"To avoid ambiguity and the diamond problem",1),
(54,14,"To prevent code reuse",0),
(55,14,"To reduce complexity",0),
(56,14,"To enforce higher memory usage",0),
-- Q15
(57,15,"To refer to current object",0),
(58,15,"To refer to parent class methods and constructors",1),
(59,15,"To create new classes",0),
(60,15,"To define abstract methods",0),
-- Q16
(61,16,"Both are the same concept",0),
(62,16,"Inheritance uses interfaces, Composition uses classes",0),
(63,16,"Inheritance is 'is-a' relationship, Composition is 'has-a'",1),
(64,16,"Composition is faster than inheritance",0),
-- Q17
(65,17,"Bundling data and methods, hiding internal details",1),
(66,17,"Inheriting from parent class",0),
(67,17,"Creating abstract methods",0),
(68,17,"Implementing interfaces",0),
-- Q18
(69,18,"By making all members public",0),
(70,18,"By removing methods from classes",0),
(71,18,"By using multiple inheritance",0),
(72,18,"By restricting access to sensitive data through private members",1),
-- Q19
(73,19,"Displaying all class members publicly",0),
(74,19,"Keeping internal implementation hidden from outside world",1),
(75,19,"Removing private keywords",0),
(76,19,"Making all attributes public",0),
-- Q20
(77,20,"They make data completely public",0),
(78,20,"They bypass encapsulation",0),
(79,20,"They provide controlled access to private data",1),
(80,20,"They prevent inheritance",0),
-- Q21
(81,21,"To prevent direct access and ensure data integrity",1),
(82,21,"To make them accessible everywhere",0),
(83,21,"To increase memory usage",0),
(84,21,"To disable inheritance",0),
-- Q22
(85,22,"They are identical concepts",0),
(86,22,"Encapsulation is for inheritance only",0),
(87,22,"Abstraction is related to data members",0),
(88,22,"Encapsulation hides data, Abstraction hides complexity",1),
-- Q23
(89,23,"Bundling data and methods",0),
(90,23,"Hiding implementation details and showing only functionality",1),
(91,23,"Acquiring properties from parent class",0),
(92,23,"Creating getter and setter methods",0),
-- Q24
(93,24,"A class with no methods",0),
(94,24,"A final class",0),
(95,24,"A class with abstract methods that cannot be instantiated",1),
(96,24,"A class with only static members",0),
-- Q25
(97,25,"A contract defining methods that implementing classes must have",1),
(98,25,"A class with inheritance",0),
(99,25,"A method in a class",0),
(100,25,"A variable declaration",0),
-- Q26
(101,26,"Interface can have state, abstract class cannot",0),
(102,26,"They are identical",0),
(103,26,"Abstract class is for multiple inheritance",0),
(104,26,"Abstract class can have state, interface is pure contract",1),
-- Q27
(105,27,"They have too many methods",0),
(106,27,"They are incomplete; abstract methods have no implementation",1),
(107,27,"They use inheritance",0),
(108,27,"They have public variables",0),
-- Q28
(109,28,"Abstraction is only for interfaces",0),
(110,28,"Abstraction removes all methods",0),
(111,28,"Car abstraction shows steering, not internal engine details",1),
(112,28,"Abstraction combines data and methods",0),
-- Q29
(113,29,"Yes, from Java 8+ with default and static methods",1),
(114,29,"No, interfaces only have abstract methods",0),
(115,29,"Only static methods have implementations",0),
(116,29,"Only private methods can be implemented",0),
-- Q30
(117,30,"private",0),
(118,30,"public",1),
(119,30,"protected",0),
(120,30,"default",0),
-- Q31
(121,31,"Only within the same class",0),
(122,31,"Anywhere in the project",0),
(123,31,"Same package and subclasses in other packages",1),
(124,31,"Only within the same package",0),
-- Q32
(125,32,"No, only public or package-private",1),
(126,32,"Yes, if it is an inner class",0),
(127,32,"Yes, always",0),
(128,32,"Only in Java 17+",0),
-- Q33
(129,33,"The member is constant and cannot change",0),
(130,33,"The member is private by default",0),
(131,33,"The member is stored in Heap memory",0),
(132,33,"The member belongs to the class, not instances",1),
-- Q34
(133,34,"Yes, using the 'this' keyword",0),
(134,34,"No, static methods cannot access instance members directly",1),
(135,34,"Yes, static methods can access anything",0),
(136,34,"Only if the instance variable is public",0),
-- Q35
(137,35,"Metaspace / Method Area",1),
(138,35,"Stack memory",0),
(139,35,"PC Register",0),
(140,35,"Local variable array",0),
-- Q36
(141,36,"To speed up code execution",0),
(142,36,"To hide code from other developers",0),
(143,36,"To handle runtime errors and prevent crash",1),
(144,36,"To compile the code faster",0),
-- Q37
(145,37,"throws is for checked; throw is for unchecked",0),
(146,37,"They are interchangeable",0),
(147,37,"throw is for classes; throws is for interfaces",0),
(148,37,"throw is for triggering; throws is for the method signature",1),
-- Q38
(149,38,"Only when an exception occurs",0),
(150,38,"Always, whether an exception is handled or not",1),
(151,38,"Only when no exception occurs",0),
(152,38,"Only if the program terminates",0),
-- Question 39
(153,39,"Handling runtime errors to prevent program crash",1),
(154,39,"Creating classes",0),
(155,39,"Managing memory allocation",0),
(156,39,"Improving compilation speed",0);

-- Q40
INSERT OR IGNORE INTO options VALUES
(157,40,"To define variables",0),
(158,40,"To stop execution permanently",0),
(159,40,"To handle exceptions and errors",1),
(160,40,"To create objects",0);
-- Q41
INSERT OR IGNORE INTO options VALUES

(161,41,"Both occur at runtime",0),
(162,41,"Checked exceptions are compile-time, unchecked are runtime",1),
(163,41,"Unchecked exceptions must be handled",0),
(164,41,"There is no difference",0);

-- Q42
INSERT OR IGNORE INTO options VALUES
(165,42,"To catch an exception",0),
(166,42,"To declare a class",0),
(167,42,"To explicitly throw an exception",1),
(168,42,"To ignore errors",0);

-- Q43
INSERT OR IGNORE INTO options VALUES
(169,43,"Process of exception propagation through call stack",1),
(170,43,"Compilation of program",0),
(171,43,"Memory allocation process",0),
(172,43,"File handling mechanism",0);

-- Q44
INSERT OR IGNORE INTO options VALUES
(173,44,"Both are same",0),
(174,44,"throw declares exception, throws handles it",0),
(175,44,"throw is used to raise exception, throws declares it",1),
(176,44,"throws is used inside catch block",0);

-- Q45
INSERT OR IGNORE INTO options VALUES
(177,45,"No, only one try block allowed",0),
(178,45,"Yes, nested try blocks are allowed",1),
(179,45,"Only in compile-time",0),
(180,45,"Only one catch block allowed",0);

-- Q46
INSERT OR IGNORE INTO options VALUES
(181,46,"Predefined system error",0),
(182,46,"Syntax error",0),
(183,46,"User-defined exception created by programmer",1),
(184,46,"Compile-time warning",0);

-- Q47
INSERT OR IGNORE INTO options VALUES
(185,47,"Yes, an interface can extend multiple interfaces",1),
(186,47,"No, interfaces only support single inheritance",0),
(187,47,"Only if they are in the same package",0),
(188,47,"Only in functional interfaces",0);

-- Q48
INSERT OR IGNORE INTO options VALUES
(189,48,"An interface that only has static methods",0),
(190,48,"A deprecated interface",0),
(191,48,"An interface with no methods used for tagging a class",1),
(192,48,"An interface used for UI design",0);

--Q49
INSERT OR IGNORE INTO options VALUES
(193,49,"protected",0),
(194,49,"private static",0),
(195,49,"package-private",0),
(196,49,"public abstract",1);

INSERT OR IGNORE INTO options VALUES

-- Q50
(197,50,"A method without implementation",1),
(198,50,"A static method",0),
(199,50,"A private method",0),
(200,50,"A constructor",0),

-- Q51
(201,51,"Yes, abstract classes can have constructors",1),
(202,51,"No, they cannot have constructors",0),
(203,51,"Only static constructors are allowed",0),
(204,51,"Only private constructors are allowed",0),

-- Q52
(205,52,"Yes, only public static final variables",1),
(206,52,"Yes, private variables",0),
(207,52,"No variables allowed",0),
(208,52,"Only protected variables",0),

-- Q53
(209,53,"extends is for inheritance, implements is for interfaces",1),
(210,53,"Both are same",0),
(211,53,"implements is used for classes only",0),
(212,53,"extends is used for interfaces only",0),

-- Q54
(213,54,"Yes, a class can implement multiple interfaces",1),
(214,54,"No, only one interface allowed",0),
(215,54,"Only abstract classes can implement interfaces",0),
(216,54,"Only interfaces can implement interfaces",0),

-- Q55
(217,55,"To achieve abstraction and multiple inheritance",1),
(218,55,"To reduce memory usage",0),
(219,55,"To avoid classes",0),
(220,55,"To replace constructors",0),

-- Q56
(221,56,"Yes, abstract class can implement an interface",1),
(222,56,"No, it cannot",0),
(223,56,"Only if it is final",0),
(224,56,"Only static classes can implement",0),

-- Q57
(225,57,"The class must be declared abstract",1),
(226,57,"The program will always compile",0),
(227,57,"Interface will be ignored",0),
(228,57,"Methods will become optional",0),

-- Q58
(229,58,"No, interfaces cannot have constructors",1),
(230,58,"Yes, always",0),
(231,58,"Only private constructors allowed",0),
(232,58,"Only default constructors allowed",0),

-- Q59
(233,59,"An interface with exactly one abstract method",1),
(234,59,"An interface with many methods",0),
(235,59,"An abstract class",0),
(236,59,"A class with static methods",0);