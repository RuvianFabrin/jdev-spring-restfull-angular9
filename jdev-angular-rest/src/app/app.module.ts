import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {FormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http'; /* Faz as requisições ajax */
import {HomeComponent} from './home/home.component';
import {RouterModule, ROUTES, Routes} from '@angular/router';
import {ModuleWithProviders} from '@angular/core';
import {LoginComponent} from './login/login.component';
import {HttpInterceptorModule} from './service/header-interceptor.service';
import {UsuarioComponent} from './componente/usuario/usuario/usuario.component';
import {UsuarioAddComponent} from './componente/usuario/usuario-add/usuario-add.component';
import {GuardiaoGuard} from './service/guardiao.guard';
import {NgxMaskModule, IConfig} from 'ngx-mask';
import {NgxPaginationModule} from 'ngx-pagination';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {NgxCurrencyModule} from 'ngx-currency';
import {UsuarioReportComponent} from './componente/usuario/usuario-report/usuario-report.component';
import {ChartsModule} from 'ng2-charts';
import { BarChartComponent } from './componente/bar-chart/bar-chart.component';

export const appRouters: Routes = [
  {path: 'home', component: HomeComponent, canActivate: [GuardiaoGuard]},
  {path: 'login', component: LoginComponent},
  {path: 'usuarioList', component: UsuarioComponent, canActivate: [GuardiaoGuard]},
  {path: 'usuarioAdd', component: UsuarioAddComponent, canActivate: [GuardiaoGuard]},
  {path: 'usuarioAdd/:id', component: UsuarioAddComponent, canActivate: [GuardiaoGuard]},
  {path: 'userReport', component: UsuarioReportComponent, canActivate: [GuardiaoGuard]},
  {path: 'chart', component: BarChartComponent, canActivate: [GuardiaoGuard]},
  {path: '', component: LoginComponent}
];

// Compilar o projeto
// ng build -c production --base-href /angular/

// Rodar fora da IDE
// npm install
// npm install -g @angular/cli
// entra na pasta do projeto
// npm install
// ng serve


export const routes: ModuleWithProviders = RouterModule.forRoot(appRouters);

export const optionsMask: Partial<IConfig> | (() => Partial<IConfig>) = {};

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    UsuarioComponent,
    UsuarioAddComponent,
    UsuarioReportComponent,
    BarChartComponent
  ],
  imports: [
    FormsModule,
    BrowserModule,
    HttpClientModule,
    routes,
    HttpInterceptorModule,
    NgxMaskModule.forRoot(optionsMask),
    NgxPaginationModule,
    NgbModule,
    NgxCurrencyModule,
    ChartsModule

  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
