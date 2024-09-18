import React from 'react';
import TableEx from './TableEx';
import './App.css';
import { Routes, Route } from 'react-router-dom';
import Home from './Components/Home';
import CreateTodoEx from './Components/CreateTodoEx';
import UploadFilesEx from './Components/UploadFilesEx';


function App() {

    return (

       <Routes>
        <Route path='/' element={<Home/>}> </Route>
        <Route path='/create-todo' element={<CreateTodoEx/>}></Route>
        <Route path='/upload-file' element={<UploadFilesEx/>}></Route>

       </Routes>
  )

}

export default App;