import { useEffect, useState } from "react";
import {Form, Button, ButtonToolbar, Input} from "rsuite";

export default function UpdateTodoEx({todo, onSubmit}){
    const[id,setId]=useState('');
    const[title,setTitle]=useState('');
    const[description,setDescription]=useState('');
    const[duedate,setDuedate]=useState('');
    const[assignee,setAssignee]=useState('');
    useEffect(()=>{
         if(todo && Array.isArray(todo) && todo.length > 0){
             
            const singleTodo = todo[0]; 
             
             setId(singleTodo.id || '');
             setTitle(singleTodo.title || '');
             setDescription(singleTodo.description || '');
             setDuedate(singleTodo.duedate || '');
             setAssignee(singleTodo.assignee || '');
         }
    },[todo]);
     const handlesubchange= (e)=> {
        e.preventDefault();
         
        onSubmit(
            {
                
                id,
                title,
                description,
                duedate,
                assignee
            }
        );
     }
    return(
        <div style={{ maxWidth: '600px', margin: '0 auto', padding: '20px', backgroundColor: '#f9f9f9', borderRadius: '8px', boxShadow: '0px 0px 10px rgba(0, 0, 0, 0.1)' }}>
        <h2 style={{ textAlign: 'center' }}>Update Todo</h2>
            <Form fluid onSubmit={handlesubchange}>
                <Form.Group controlId="title">
                 <Form.ControlLabel>Title</Form.ControlLabel>
                    <Form.Control name="title" value={title} onChange={(e)=> setTitle(e.target.value)} accepter={Input}/>
                </Form.Group>
                <Form.Group controlId="description">
                <Form.ControlLabel>description</Form.ControlLabel>
                    <Form.Control name="description" value={description} onChange={(e)=> setDescription(e.target.value)} accepter="textarea" rows={3}/>
                </Form.Group>
                <Form.Group controlId="duedate">
                <Form.ControlLabel>duedate</Form.ControlLabel>
                    <Form.Control name="duedate" value={duedate} onChange={(e)=> setDuedate(e.target.value)} oneTap/>
                </Form.Group>
                <Form.Group controlId="assignee">
                <Form.ControlLabel>assignee</Form.ControlLabel>
                    <Form.Control name="assignee" value={assignee} onChange={(e)=> setAssignee(e.target.value)} accepter={Input}/>
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