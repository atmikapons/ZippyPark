const express = require('express');
const mysql = require('mysql');
const http = require('http');
const path = require('path');
const app = express();
const bodyParser = require('body-parser');

app.set('view engine', 'ejs');
app.set('views', path.join(__dirname, 'views'));
app.use(express.static(path.join(__dirname, 'public')));
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json()); // parse form data client
app.use(express.static(path.join(__dirname, 'public'))); // configure express to use public folder

// database connection
var db = mysql.createConnection({
    host: '127.0.0.1', // local host
    user: 'zippypark',
    password: 'goyhVynIiLcJgHpN',
    database: 'zippypark'
});

/// Kylie's remote database for testing:
// const db = mysql.createConnection({
//     host: 'remotemysql.com',
//     user: 'KUU6GpyPt4',
//     password: 'XIbBmfeIWj',
//     database: 'KUU6GpyPt4',
//     port: 3306
// });

// // Sam's remote database for testing:
// const db = mysql.createConnection({
//     host: 'remotemysql.com',
//     user: 'dhI47C5XFR',
//     password: 'R5EYIRUesL',
//     database: 'dhI47C5XFR',
//     port: 3306
// });

db.connect( function (err) {
    if ( err ) {
        console.error('error connecting: ' + err.stack);
        return;
    }
    //console.log('connected as id ' + db.threadId);
});

const routes = require('./routes/index.js')(app,db);

const httpServer = http.createServer( app );
httpServer.listen(8080); // can change port

module.exports = app; // for testing