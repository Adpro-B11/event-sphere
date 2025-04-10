Untuk fitur **Manajemen Acara**, saya menggunakan **Command Pattern** sebagai design pattern utama. Command Pattern digunakan untuk membungkus setiap operasi pengelolaan acara, seperti membuat acara baru (`CreateEventCommand`), memperbarui informasi acara (`UpdateEventCommand`), dan menghapus acara (`DeleteEventCommand`), ke dalam objek command terpisah. Tujuan dari penggunaan Command Pattern ini adalah untuk memisahkan logika eksekusi dari pemanggilnya, sehingga kode menjadi lebih modular, mudah diuji, dan scalable. Dengan pendekatan ini, setiap use case seperti pembuatan acara oleh Organizer, pembaruan informasi acara, atau penghapusan acara dapat direpresentasikan sebagai command mandiri, memungkinkan penambahan fitur baru tanpa mengubah kode inti yang sudah ada (open-closed principle). Selain itu, Command Pattern juga mendukung implementasi fitur seperti undo/redo, logging, atau queueing di masa mendatang, yang dapat berguna dalam skenario manajemen acara seperti ini.