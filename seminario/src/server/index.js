import grpc from '@grpc/grpc-js';
import noteService from './note-service.js';

const server = new grpc.Server();

/**
 * Graceful shutdown
 */
process.on('SIGINT', () => server.tryShutdown((_) => {}));

/**
 * Registro do serviço
 */
server.addService(...noteService);

/**
 * Inicialização do server
 */
server.bindAsync('0.0.0.0:50051', grpc.ServerCredentials.createInsecure(), (_, port) => {
    server.start();
    console.log(`Server started on port ${port}`);
});
