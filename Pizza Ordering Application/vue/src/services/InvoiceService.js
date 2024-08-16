import axios from 'axios';

const http = axios.create({
  baseURL: import.meta.env.VITE_REMOTE_API
});



export default {
    sendOrder(order) {
      let token = localStorage.getItem('token')
      console.log(token)
        return http.post('/invoice', 
          order,
          {
          headers: {
            'Authorization': `Bearer ${token}`
          }
    })
      },

    getOrders(userToken) {
      let token = localStorage.getItem('token')
      return http.get('/invoice', {
        headers: {
          'Authorization': `Bearer ${userToken ? userToken : token}`
        }
      })
    },
    getOrderByInvoiceId(id) {
      let token = localStorage.getItem('token')
      return http.get(`/invoice/${id}`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      })
    },
    updateOrder(id, invoice) {
      let token = localStorage.getItem('token')
      console.log(id)
      return http.put(`/invoice/${id}`, 
        invoice, 
        {
          headers: {
          'Authorization': `Bearer ${token}`
        }
      })
    }
}