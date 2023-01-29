# Java-Projects-Assignments
In this repository you can find my solutions to my java assignments throughout my degree. I will be aiming to add every single one of them as I am just now creating this repo 7 months after graduating :)

## Corrupted Database

Introduction
Your organisation has asked you to perform an important task. A database system that stores customer details has become badly corrupted. The result is that, as things stand, you have lost the email addresses of all of your customers and that was the primary method that was used to communicate with them. All that remains of the database table that contained the email addresses is a text file called ‘corrupteddb’. If you open this file you will that, whilst it is readable to some extent, it now has no structure. There is no way that the database management system will be able to read this file. Your task is to write an application that will extract as many email addresses as possible from the file. The organisation tells you that all of its customers resided in the USA, UK, Germany, Romania or Japan.

The email addresses
Email addresses have two parts to them, separated by the ‘@’ symbol: the local-part and the domain-part. In the following examples, the string before the ‘@’ symbol is the local-part and the string after the ‘@’ is the domain-part:
sole survivor@sanctuaryhills.com
axlrose6@gunsnroses.net
gumball@amazing.world.of.jp

Some local-parts will have a separator. In the above examples, ‘sole survivor’ has an underscore character ‘ ’ as a separator.
In the following description, the local-part and the domain-part will be described as having components. The local-part can have more than one component, separated by a separator. The domain part will have at least two components, separated by a separator. The final part of the domain-part, which follows the final period (‘.’) character is called the top-level domain.

However, to simplify your task, you have been told that you only need to attempt to find email addresses that conform to the following format:

1. Local-part
The local-part will contain only:
 Upper-case and lower-case letters.
 The digits 0-9.
 The underscore character ‘ ‘. This can appear anywhere in the local-part.
 The period ‘.’ character, with the restriction that this is only used as a separator. The period character cannot appear at the beginning or end of the local-part. There will be a maximum of two components to the local-part. For example, ‘gumball’ has one component whilst ‘bill.gates’ has two. There will be, therefore, at most one period character in the local-part.

2. Domain-part
The domain-part will contain only:
 Lower-case letters.
 The period ‘.’ character, with the restriction that this is only used as a separator. Each domain-part
will consist of either two or three components separated by a period.
You are also allowed to make the following assumption:
 Due to the information given about about the residencies of the customers, all domain-parts will top-level domain that is from the following set: {net, com, uk, de, jp, ro}. No other top-level domain is valid.

When your program is looking for email addresses in the file, it does not need to try and ‘rescue’ email addresses by correcting them. You do not have enough information to do that. Although some email addresses will look obviously wrong to you, your program should still find those email addresses. Your program may well find ‘email addresses’ that do not even exist (see next section) but are simply artefacts of the corrupted file. That is OK. However, see the remarks below about embedded addresses. Your only task is to find strings that conform to the rules set out above. You are not expected to make a judgement about whether some email address is real or not.
