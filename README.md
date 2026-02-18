# AWS Leaderboard

This is a home project developed by Gabor Havasi, for his sister, Virag. During the project I used Claude a lot, ngl. This readme meant to create notes for myself too during development.
* The backend is a `Node` server with `Express` in `typescript` and a `Firebase DB (?)`.
* As a frontend I chose `Android`, because that is my main field.

# Specification

Here comes a lot of details about the project.

## Frontend

This is an Android project, I did not start it yet.

## Backend

The server of the project can be found in the backend folder of this repository. As above mentioned, it is `Node`, `Express` and `Firebase DB` in `typescript`.

Init steps:
1. `npm init -y` — Creates package.json to manage dependencies.
2. `npm install express`
3. `npm install -D typescript ts-node @types/node @types/express nodemon`
    * **typescript** — the TS compiler
    * **ts-node** — lets you run TypeScript directly without compiling first (great for development)
    * **@types/node and @types/express** — type definitions
    * **nodemon** — hot reload
5. `npx tsc --init` — use the TypeScript compiler to generate a config file for this project

From here I just copy paste code from Claude to VS Code, and try to understand as much as I can.

### Authentication

JWT tokens ([What else?](https://www.youtube.com/watch?v=DfyeXrdZZ1o&t=46s))
1. `npm install bcrypt jsonwebtoken`
    * **bcrypt** — for hashing passwords (you never store plain text passwords)
    * **jsonwebtoken** — for creating and verifying JWT tokens
2. `npm install -D @types/bcrypt @types/jsonwebtoken`

### Database

Firestore Database



<br><br><br><br><br><br>
# I keep these md examples for the future
*This text will be italic*  
_This will also be italic_

**This text will be bold**  
__This will also be bold__

_You **can** combine them_

## Lists

### Unordered

* Item 1
* Item 2
* Item 2a
* Item 2b
    * Item 3a
    * Item 3b

### Ordered

1. Item 1
2. Item 2
3. Item 3
    1. Item 3a
    2. Item 3b

## Images

![This is an alt text.](/image/Markdown-mark.svg "This is a sample image.")

## Links

You may be using [Markdown Live Preview](https://markdownlivepreview.com/).

## Blockquotes

> Markdown is a lightweight markup language with plain-text-formatting syntax, created in 2004 by John Gruber with Aaron Swartz.
>
>> Markdown is often used to format readme files, for writing messages in online discussion forums, and to create rich text using a plain text editor.

## Tables

| Left columns  | Right columns |
| ------------- |:-------------:|
| left foo      | right foo     |
| left bar      | right bar     |
| left baz      | right baz     |

## Blocks of code

```
let message = 'Hello world';
alert(message);
```

## Inline code

This web site is using `markedjs/marked`.
