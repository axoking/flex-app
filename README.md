# FLEX App

This is just some side project of mine. Flex is shorthand for **F**i**l**e **Ex**change and it's gonna be basically AirDrop but Cross-Platform and using the local network. The project is written in Kotlin.

## The idea

Everyone who has Flex shall be able to **send** and **receive** files from everyone in their local network, even if they don't have Flex. One with the Flex app can host a server
on their device which can either **provide files that other people in their network can download** (*server-push* method) or **wait for others to send files** (*server-pull* method).
In both cases, a server is opened on **Port 56789**. Other people in the same LAN can see the server in their Flex App and download/upload files from/to it, and the server also offers
a web interface for those who don't have the Flex app.

## The current state
The project is in the very beginning and at first the core functionality of Flex will be developed. Then, the desktop GUI frontend and web interface will follow. After some testing with that,
Android and maybe also IOS will be targeted.


```
 _________________
< i hate java tbh >
 -----------------
\                             .       .
 \                           / `.   .' "
  \                  .---.  <    > <    >  .---.
   \                 |    \  \ - ~ ~ - /  /    |
        _____          ..-~             ~-..-~
       |     |   \~~~\.'                    `./~~~/
      ---------   \__/                        \__/
     .'  O    \     /               /       \  "
    (_____,    `._.'               |         }  \/~~~/
     `----.          /       }     |        /    \__/
           `-.      |       /      |       /      `. ,~~|
               ~-.__|      /_ - ~ ^|      /- _      `..-'
                    |     /        |     /     ~-.     `-. _  _  _
                    |_____|        |_____|         ~ - . _ _ _ _ _>
```
