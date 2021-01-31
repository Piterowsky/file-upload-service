import React, { useRef, useState } from 'react';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import Alert from 'react-bootstrap/Alert';
import InputGroup from 'react-bootstrap/InputGroup';
import FormControl from 'react-bootstrap/FormControl';

const uploadFileForm = () => {
    const [selectedFile, setSelectedFile] = useState(null);
    const [alertStatusValue, setStatusAlertValue] = useState('');
    const [alertVariant, setAlertVariant] = useState('success');
    const [isColorTextFieldDisabled, setIsColorTextFieldDisabled] = useState(true);
    const colorInput = useRef(null);

    const sendFileToApi = (e) => {
        e.preventDefault();

        const formData = new FormData();
        formData.append('file', selectedFile);
        const url = process.env.API_BASE_URL + '/api/upload/' + colorInput.current.value;
        fetch(url, { method: 'POST', body: formData })
            .then((response) => response.json())
            .then((result) => {
                if (result.countOfSavedCars > 0) {
                    setStatusAlertValue(result.message + ': ' + result.countOfSavedCars);
                    setAlertVariant('success');
                } else {
                    setStatusAlertValue(result.message);
                    setAlertVariant('warning');
                }
            });
    };

    const onFileChange = (e) => {
        e.target.classList.remove('is-invalid');
        setSelectedFile(e.target.files[0]);
        e.target.parentNode.querySelector('#submitButton').focus();
    };

    const getSelectedFileName = () => {
        return selectedFile ? selectedFile.name : '';
    };

    const colorFieldToggle = () => {
        setIsColorTextFieldDisabled(!isColorTextFieldDisabled);
    };

    const getResponseAlert = () => {
        return alertStatusValue !== '' && <Alert variant={alertVariant}>{alertStatusValue}</Alert>;
    };

    return (
        <Form onSubmit={sendFileToApi}>
            <div className="mb-3">
                <Form.File id="file-input" custom>
                    <Form.File.Input isInvalid onChange={onFileChange} style={{ marginBottom: '5px' }} />
                    <Form.File.Label data-browse="Select file">{getSelectedFileName()}</Form.File.Label>
                    <Form.Control.Feedback type="invalid">You have to choose file</Form.Control.Feedback>

                    <InputGroup className="mb-3">
                        <InputGroup.Prepend>
                            <InputGroup.Checkbox aria-label="Checkbox for color input" onChange={colorFieldToggle} />
                            <InputGroup.Text id="color-input-label" aria-label="Text input for filtering cars by color">
                                Color
                            </InputGroup.Text>
                        </InputGroup.Prepend>
                        <FormControl
                            ref={colorInput}
                            aria-label="Color"
                            aria-describedby="color-input-label"
                            disabled={isColorTextFieldDisabled}
                            placeholder={isColorTextFieldDisabled ? 'disabled' : ''}
                        />
                    </InputGroup>

                    {getResponseAlert()}

                    <Button id="submitButton" type="submit" disabled={selectedFile === null}>
                        Send file
                    </Button>
                </Form.File>
            </div>
        </Form>
    );
};

export default uploadFileForm;
