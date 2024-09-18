import React from "react";
import 'rsuite/dist/rsuite.min.css';
import { Table, Column, HeaderCell, Cell} from "rsuite-table";
import { useEffect, useState } from "react";
import axios from "axios";
import { Button, ButtonGroup } from "rsuite";
import './App.css';
 

function TableEx({onHandleUpdate}){
    const[data, setData]=useState([]);
    const[currentpage,setCurrentPage]=useState(1);
    const[mintodosperpage]=useState(10);
    useEffect(()=>{
        async function getTodos(){
            const offset=(currentpage-1)*mintodosperpage;
            let result=await axios.get('http://localhost:8080/backend/todos',{
                params:{
                    limit: mintodosperpage,
                    offset: offset,
                }
            });
            setData(result.data);
        }
        getTodos()
    },[currentpage])
    const handlenext= ()=>{
        setCurrentPage(currentpage+1);
    }
    const handleprev=()=>{
        if(currentpage>1){
            setCurrentPage(currentpage-1);
        }
    }
    const handleDelete = async (id)=>{
        try{
       const response= await axios.delete(`http://localhost:8080/backend/todos/${id}`)
       if(response.status===200){
        setData(data.filter(item => item.id !== id));
       }else{
        console.error("failed to delete todo")
       }
    } catch (error) {
        console.error('Error:', error);
      }
    
    }

    return(
    <div style={{ padding: '20px', backgroundColor: '#f9f9f9', borderRadius: '8px', boxShadow: '0px 0px 10px rgba(0, 0, 0, 0.1)' }}>
    <Table className="rs-table" data={data} bordered autoHeight>
        <Column width={70} align="center" fixed>
        <HeaderCell>ID</HeaderCell>
        <Cell dataKey="id" />
      </Column>
        <Column flexGrow={1} align='center'>
       <HeaderCell style={{fontSize: '18px'}} >title</HeaderCell>
       <Cell dataKey="title"></Cell>
       </Column>
       <Column flexGrow={1} align='center'>
       <HeaderCell style= {{fontSize: '18px'}}>description</HeaderCell>
       <Cell dataKey="description"></Cell>
       </Column>
       <Column flexGrow={1} align='center'>
       <HeaderCell style={{fontSize:'18px'}}>duedate</HeaderCell>
       <Cell dataKey="duedate"></Cell>
       </Column>
       <Column flexGrow={1} align='center'>
       <HeaderCell style={{fontSize:'18px'}}>assignee</HeaderCell>
       <Cell dataKey="assignee"></Cell>
       </Column>
       <Column flexGrow={1} align='center'>
       <HeaderCell style={{fontSize:'18px'}}>Actions</HeaderCell>
       <Cell>
        {rowData => (
        <ButtonGroup size="xs">
            <Button onClick={()=> handleDelete(rowData.id)} appearance="primary" color="red">delete</Button>
            <button onClick={()=> onHandleUpdate(rowData.id)} appearance="default">update</button>
        </ButtonGroup>
       )}
       </Cell>
       </Column>
    </Table>
    <div style={{ textAlign: 'center', marginTop: '20px' }}>
    <Button onClick={handlenext} disabled={data.length<mintodosperpage} className="pagination-button next">next</Button>
    <Button onClick={handleprev} disabled={currentpage===1} className="pagination-button prev">prev</Button>
    </div>
    </div>
    )
}

export default TableEx;