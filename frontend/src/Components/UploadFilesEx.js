import React from "react";
import { Button } from "rsuite";
import { useState } from "react";
import axios from "axios";

export default function UploadFilesEx(){
   const[selectedfile,setfile]=useState(null);
   const handlefile=(e)=>{
     
    setfile(e.target.files[0]);
   }
   const onHandleSubmit= async (e)=>{
     e.preventDefault();
     const formData=new FormData();
     console.log(selectedfile);
     formData.append("file",selectedfile);
     console.log(formData);
        await axios.post('http://localhost:8080/backend/files',formData,{
            headers:{
                'Content-Type': 'multipart/form-data'
            }
        }).then(response => {
            console.log('File uploaded successfully:', response.data);
        })
        .catch(error => {
            console.error('Error uploading file:', error);
        });
   }
    return(
        <div>
            <form onSubmit={onHandleSubmit}>
            <h1>upload a csv file</h1><br/>
            <input type="file" onChange={handlefile}/><br/><br/>
            <Button type="submit">upload</Button>
            </form>
        </div>
    )
}