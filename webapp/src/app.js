import React from 'react';
import ReactDOM from 'react-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import uploadFileForm from './uploadFileForm';
import { Jumbotron } from 'react-bootstrap';

const App = () => {
    const containerStyles = {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        minHeight: '100vh',
        minWidth: '100wh',
    };

    const jumbotronStyles = {
        minWidth: '30vw',
        minHeight: '35vh',
    };

    return (
        <div style={containerStyles}>
            <Jumbotron style={jumbotronStyles}>
                <uploadFileForm />
            </Jumbotron>
        </div>
    );
};

ReactDOM.render(<App />, document.getElementById('root'));
