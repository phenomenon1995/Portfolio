import axios from 'axios';

const http = axios.create({
  baseURL: import.meta.env.VITE_REMOTE_API
});



export default {

    validateAddress(address) {
      let token = localStorage.getItem('token')
      return http.post(`/validate`, 
        address
      ,
      {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      })
    }
    
}