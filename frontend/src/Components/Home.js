import React from 'react';
import TableEx from '../../src/TableEx';
import { useNavigate } from 'react-router-dom';
 import { useState } from 'react';
import axios from 'axios';
import UpdateTodoEx from './UpdateTodoEx';
import { Button } from 'rsuite';
import '../App.css';

function Home() {
   const[isUpdating,setIsUpdating]=useState(false);
   const[selectedTodo,setSelectedTodo]=useState(null);
   const navigate=useNavigate();
   const handleUpdate= async (id)=>{
      const response=await axios.get(`http://localhost:8080/backend/todos/${id}`);
        
      setSelectedTodo(response.data);
       
      setIsUpdating(true);
       
   }
   const onHandleSubmit= async (todo)=>{
      const id = todo.id;
       
      const result=await axios.put(`http://localhost:8080/backend/todos/${id}`,todo,
         {headers: {
         'Content-Type': 'application/json',
       },}
      )
      if (result.status === 200) {
         setIsUpdating(false);
         setSelectedTodo(null);
         // Optionally refresh the todo list or handle UI updates
       } else {
         console.error('Failed to update todo');
       }
   }
   const handleexport=async ()=>{
       const result= await axios.get('http://localhost:8080/backend/todoFile');
      if (result.status === 200) {
         console.log('Data exported successfully.');
     } else {
         console.log('Failed to export data.');
    }
   }
    return (
       <div>
        <h2 style={{ textAlign: 'center', fontSize: '28px', fontWeight: 'bold', color: '#333', marginBottom: '20px', paddingBottom: '10px', borderBottom: '2px solid #ccc'}}>List of Todos</h2>
        <div className='container'>
        <Button onClick={()=> navigate('create-todo')} className="button create-todo-button">create</Button>
        <Button onClick={handleexport} className="button export-button">export</Button>
        </div>
        {isUpdating? (<UpdateTodoEx todo={selectedTodo} onSubmit={onHandleSubmit}/>): (<TableEx onHandleUpdate={handleUpdate}/>)}
            
       </div>
  )

}

export default Home;