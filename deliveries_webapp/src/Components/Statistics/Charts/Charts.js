import React from 'react';
import {Line, Bar} from 'react-chartjs-2';


function Chart(props){
    return(
        <>
            {props.chart === "Bar" && 
                <Bar
                data={{
                    labels: props.label,
                    datasets: [{
                        label: '# of Votes',
                        data: props.data,
                        backgroundColor: [
                            'rgba(255, 99, 132, 0.2)',
                            'rgba(54, 162, 235, 0.2)',
                            'rgba(255, 206, 86, 0.2)',
                            'rgba(75, 192, 192, 0.2)',
                        ],
                        borderColor: [
                            'rgba(255, 99, 132, 1)',
                            'rgba(54, 162, 235, 1)',
                            'rgba(255, 206, 86, 1)',
                            'rgba(75, 192, 192, 1)',
                        ],
                        borderWidth: 1
                    }]
                }}
                options= {{
                    responsive: true,
                    maintainAspectRatio: false,
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    }
                    
                }}
                />
            }

            {props.chart === "Line" && 
                <Line
                data={{
                    labels: props.label,
                    datasets: [{
                        label: '# of Votes',
                        data: props.data,
                        borderColor: [
                            'rgba(255, 99, 132, 1)',
                        ],
                        borderWidth: 1,
                        tension: 0.1
                    }]
                }}
                options= {{
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    }
                }}
                />
            }

            
    
        </>
        
    );
}

export default Chart;