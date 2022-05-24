// chart1
var context = document
.getElementById('myChart1')
.getContext('2d');
var myChart1 = new Chart(context, {
type: 'line', // 차트의 형태
data: { // 차트에 들어갈 데이터
    labels: [
        //x 축
        '1','2','3','4','5','6','7'
    ],
    datasets: [
        { //데이터
            label: 'test1', //차트 제목
            fill: false, // line 형태일 때, 선 안쪽을 채우는지 안채우는지
            data: [
                21,19,25,20,23,26,70 //x축 label에 대응되는 데이터 값
            ],
            backgroundColor: 'rgba(255, 99, 132, 1)',
            borderColor: 'rgba(255, 99, 132, 1)',
            borderWidth: 1
        },
        {
            label: 'test2',
            fill: false,
            data: [
                8, 34, 12, 24, 21, 19, 25
            ],
            backgroundColor: 'rgb(121,82,179)',
            borderColor: 'rgb(121,82,179)',
            borderWidth: 1
        }
    ]
},
options: {
    scales: {
        yAxes: [
            {
                ticks: {
                    beginAtZero: true
                }
            }
        ]
    }
}
});

// chart2
var context = document
.getElementById('myChart2')
.getContext('2d');
var myChart2 = new Chart(context, {
type: 'line',
data: {
    labels: [
        '1','2','3','4','5','6','7'
    ],
    datasets: [
        { //데이터
            label: 'test1',
            fill: false,
            data: [
                1,80,25,50,23,30,70
            ],
            backgroundColor: 'rgba(255, 99, 132, 1)',
            borderColor: 'rgba(255, 99, 132, 1)',
            borderWidth: 1
        },
        {
            label: 'test2',
            fill: false,
            data: [
                8, 34, 12, 24, 21, 19, 25
            ],
            backgroundColor: 'rgb(121,82,179)',
            borderColor: 'rgb(121,82,179)',
            borderWidth: 1
        }
    ]
},
options: {
    scales: {
        yAxes: [
            {
                ticks: {
                    beginAtZero: true
                }
            }
        ]
    }
}
});

// chart3
var context = document
.getElementById('myChart3')
.getContext('2d');
var myChart3 = new Chart(context, {
type: 'doughnut', // 차트의 형태
data: {
    datasets: [
        { //데이터
            data: [30,5,5,5,10,35,10],
            backgroundColor: ['red', 'orange', 'yellow', 'green', 'blue','purple','silver']
        }
    ]
},
options: {
    scales: {
        yAxes: [
            {
                ticks: {
                    beginAtZero: true
                }
            }
        ]
    }
}
});