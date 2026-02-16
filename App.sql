CREATE TABLE IF NOt EXISTS topics(
        topic_no INTEGER NOT NULL PRIMARY  KEY,
        topic_name TEXT NOT NUll UNIQUE);
INSERT OR IGNORE INTO topics(topic_no,topic_name)
VALUES(1,"Polymorphism");
INSERT OR IGNORE INTO topics(topic_no,topic_name)
VALUES(2,"Inheritance");
INSERT OR IGNORE INTO topics(topic_no,topic_name)
VALUES(3,"Encapsulation");
INSERT OR IGNORE INTO topics(topic_no,topic_name)
VALUES(4,"Abstraction");



CREATE  TABLE IF NOT EXISTS questions(
    question_id  INTEGER NOT NULL PRIMARY KEY,
    topic_id  INTEGER NOT NULL,
    question_text TEXT NOT NULL,
    difficulty TEXT NOT NULL,
    FOREIGN KEY(topic_id) REFERENCES topics(topic_no)
    );
 INSERT OR IGNORE INTO questions(question_id,topic_id,question_text,difficulty)
    VALUES(1,1,"What is polymorphism in OOP ?","Easy");
INSERT OR IGNORE INTO questions(question_id,topic_id,question_text,difficulty)
    VALUES(2,1,"What the main two types of polymorphism?","Easy");
    INSERT OR IGNORE INTO questions VALUES
(3,1,"Explain compile-time polymorphism with an example.","Medium"),
(4,1,"Explain runtime polymorphism in Java.","Medium"),
(5,1,"How does method overloading support polymorphism?","Easy"),
(6,1,"Why is polymorphism important in OOP?","Easy"),
(7,1,"Explain polymorphism using a real-life example.","Medium"),
(8,1,"What is method overriding? How does it relate to polymorphism?","Medium"),
(9,1,"Can constructors be polymorphic? Explain.","Hard");
INSERT OR IGNORE INTO questions VALUES
(10,2,"What is inheritance in OOP?","Easy"),
(11,2,"What are the advantages of inheritance?","Easy"),
(12,2,"Explain single inheritance.","Easy"),
(13,2,"Explain multilevel inheritance with an example.","Medium"),
(14,2,"Why is multiple inheritance not supported in Java classes?","Medium"),
(15,2,"What is the use of the super keyword?","Medium"),
(16,2,"Differentiate between inheritance and composition.","Hard");
INSERT OR IGNORE INTO questions VALUES
(17,3,"What is encapsulation in OOP?","Easy"),
(18,3,"How does encapsulation improve security?","Easy"),
(19,3,"Explain data hiding.","Easy"),
(20,3,"How are getters and setters related to encapsulation?","Medium"),
(21,3,"Why are class variables usually private?","Medium"),
(22,3,"Encapsulation vs abstraction—explain.","Hard");
INSERT OR IGNORE INTO questions VALUES
(23,4,"What is abstraction in OOP?","Easy"),
(24,4,"What is an abstract class?","Easy"),
(25,4,"What is an interface?","Easy"),
(26,4,"Difference between abstract class and interface.","Medium"),
(27,4,"Why can’t we create objects of an abstract class?","Medium"),
(28,4,"Explain abstraction with a real-world example.","Medium"),
(29,4,"Can an interface have method implementations? Explain.","Hard");

CREATE TABLE IF NOT EXISTS options(
option_id INTEGER NOT NULL PRIMARY KEY,
question_id INTEGER NOT NULL,
option_text TEXT NOT NULL,
is_correct INTEGER NOT NULL,
FOREIGN KEY (question_id) REFERENCES questions(question_id));

INSERT OR IGNORE INTO options VALUES
(1,1,"Ability of an object to take multiple forms",1),
(2,1,"Process of hiding data",0),
(3,1,"Binding data and methods together",0),
(4,1,"Creating multiple classes",0);

INSERT OR IGNORE INTO options VALUES
(5,2,"Compile-time and Runtime polymorphism",1),
(6,2,"Static and Dynamic memory allocation",0),
(7,2,"Single and Multiple inheritance",0),
(8,2,"Abstraction and Encapsulation",0);

INSERT OR IGNORE INTO options VALUES
(9,3,"Method overloading is an example of compile-time polymorphism",1),
(10,3,"Method overriding is compile-time polymorphism",0),
(11,3,"Interfaces provide compile-time polymorphism",0),
(12,3,"Abstract classes use compile-time polymorphism",0);

INSERT OR IGNORE INTO options VALUES
(13,4,"Method overriding enables runtime polymorphism",1),
(14,4,"Method overloading enables runtime polymorphism",0),
(15,4,"Constructors provide runtime polymorphism",0),
(16,4,"Variables support runtime polymorphism",0);

INSERT OR IGNORE INTO options VALUES
(17,5,"Same method name with different parameter lists",1),
(18,5,"Same method name with same parameters",0),
(19,5,"Different method names with same parameters",0),
(20,5,"Same return type only",0);

INSERT OR IGNORE INTO options VALUES
(21,6,"It increases code flexibility and reusability",1),
(22,6,"It reduces memory usage",0),
(23,6,"It removes inheritance",0),
(24,6,"It prevents method overriding",0);

INSERT OR IGNORE INTO options VALUES
(25,7,"Same person behaving differently in different roles",1),
(26,7,"One object hiding its data",0),
(27,7,"Multiple classes sharing data",0),
(28,7,"Accessing private variables",0);

INSERT OR IGNORE INTO options VALUES
(29,8,"Subclass redefining a superclass method",1),
(30,8,"Same method name with different parameters",0),
(31,8,"Calling multiple constructors",0),
(32,8,"Using private methods",0);

INSERT OR IGNORE INTO options VALUES
(33,9,"No, constructors cannot be overridden",1),
(34,9,"Yes, constructors support runtime polymorphism",0),
(35,9,"Yes, constructors are inherited",0),
(36,9,"Only static constructors are polymorphic",0);