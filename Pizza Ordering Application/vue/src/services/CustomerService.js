import axios from 'axios';

const http = axios.create({
  baseURL: import.meta.env.VITE_REMOTE_API
});



export default {
    getCustomer(id) {
      let token = localStorage.getItem('token')
        console.log(id)
        return http.get(`/customer/${id}`,
        {
            headers: {
              'Authorization': `Bearer ${token}`
            }
          })
    },
    addCustomer(customer) {
      let token = localStorage.getItem('token')
        return http.post('/customer', 
        customer, 
        {
          headers: {
          'Authorization': `Bearer ${token}`
        }
      })
    }
}