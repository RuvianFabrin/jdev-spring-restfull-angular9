export class AppConstants {
  public static get baseServidor(): string {
    return 'http://localhost:8085/';
  }

  public static get baseLogin(): string {
    return this.baseServidor + 'jdevrestapi/login';
  }

  public static get baseUrl(): string {
    return this.baseServidor + 'jdevrestapi/usuario/';
  }

  public static get getBaseUrlPath(): string {
    return this.baseServidor + 'jdevrestapi/';
  }
}
