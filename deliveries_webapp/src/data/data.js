const urlAPI = process.env.NODE_ENV === "development" ? 'http://localhost:8080/' : 'http://192.168.160.233:8080/';
const urlWeb = process.env.NODE_ENV === "development" ? 'http://localhost:3000/' : 'http://192.168.160.233:80/';

export {urlAPI, urlWeb};
