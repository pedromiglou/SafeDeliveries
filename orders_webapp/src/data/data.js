const urlAPI = process.env.NODE_ENV === "development" ? 'http://localhost:8081/' : 'http://192.168.160.233:8081/';
const urlWeb = process.env.NODE_ENV === "development" ? 'http://localhost:3001/' : 'http://192.168.160.233:81/';

export {urlAPI, urlWeb};
