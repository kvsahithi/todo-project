import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { Form, Button, ButtonToolbar} from 'rsuite';
export default function CreateTodoEx(){
    const[title,setTitle]=useState('');
    const[description,setDescription]=useState('');
    const[duedate,setDuedate]=useState('');
    const[assignee,setAssignee]=useState('');
    const navigate=useNavigate();

    const handleSubmit=async (e)=>{
        e.preventDefault();
        if(!title|| !description|| !duedate|| !assignee) return;
        const todo={
            title,
            description ,
            duedate ,
            assignee
        }
        try{
        const response= await axios.post('http://localhost:8080/backend/todos',todo, {
           Headers: {
               'Content-Type':'application/json',
            },
        })
         
        if (response.status === 200) {
            navigate('/'); // Navigate on success
          } else {
            console.error('Unexpected response status:', response.status);
          }
    } catch(error){
        console.error('Error:', error); 
    }
    }
    
    return(
        <div style={{ maxWidth: '500px', margin: '0 auto', padding: '20px', backgroundColor: '#f9f9f9', borderRadius: '8px', boxShadow: '0px 0px 10px rgba(0, 0, 0, 0.1)' }}>
            <h2 style={{ textAlign: 'center', marginBottom: '20px' }}>Create a new Todo</h2>

            <Form layout="horizontal" onSubmit={handleSubmit}>
               <Form.Group>
                <Form.ControlLabel>Title</Form.ControlLabel>
                <Form.Control name="title" value={title} onChange={(e)=>{setTitle(e.target.value)}} placeholder="Enter todo title" required/>
               </Form.Group>
               <Form.Group>
                <Form.ControlLabel>Description</Form.ControlLabel>
                <Form.Control name="description" value={description} componentClass="textarea" rows={3} onChange={(e)=>{setDescription(e.target.value)}} placeholder="enter description of todo" required/>
                </Form.Group>
                <Form.Group>
                <Form.ControlLabel>Duedate</Form.ControlLabel>
                <Form.Control name="date" type="date" value={duedate} onChange={(e)=>{setDuedate(e.target.value)}}  />
                </Form.Group>
                <Form.Group>
                <Form.ControlLabel>Assignee</Form.ControlLabel>
                <Form.Control name="assignee" value={assignee} onChange={(e)=>{setAssignee(e.target.value)}} placeholder="Assign this todo to someone" />
                </Form.Group>
                <Form.Group>
                <ButtonToolbar>
                <Button appearance="primary" type="submit">submit</Button>
                <Button appearance="default">cancel</Button>
                </ButtonToolbar>
                </Form.Group>
            </Form>

             
        </div>
    );
}
