import {Component, OnInit} from '@angular/core';
import {ChartDataSets, ChartOptions, ChartType} from 'chart.js';
import {Label} from 'ng2-charts';
import {UsuarioService} from '../../service/usuario.service';
import {UserChart} from '../../model/UserChart';

@Component({
  selector: 'app-bar-chart',
  templateUrl: './bar-chart.component.html',
  styleUrls: ['./bar-chart.component.css']
})
export class BarChartComponent implements OnInit {

  constructor(private usuarioService: UsuarioService) {
  }

  userChart: UserChart = new UserChart();

  ngOnInit() {
    this.usuarioService.carregarGrafico().subscribe(data => {
      this.userChart = data;

      this.barChartLabels = this.userChart.nome.split(',');
      const arraySalario = JSON.parse('[' + this.userChart.salario.split(',') + ']');
      this.barChartData = [
        {data: arraySalario, label: 'Salário Usuário'}
      ];
    });
  }

  barChartOptions: ChartOptions = {
    responsive: true,
  };

  barChartLabels: Label[];
  barChartType: ChartType = 'bar';
  barChartLegend = true;
  barChartPlugins = [];

  barChartData: ChartDataSets[] = [
    {data: [], label: 'Salário Usuário'}
  ];


}
