import grpc from '@grpc/grpc-js';
import protoLoader from '@grpc/proto-loader';
import path from 'node:path';
import { fileURLToPath } from 'node:url';

/**
 * Carregamento do proto dinamicamente
 */
const __dirname = path.dirname(fileURLToPath(import.meta.url));
const protoObject = protoLoader.loadSync(path.resolve(__dirname, '../../proto/note-service.proto'));
const noteDefinition = grpc.loadPackageDefinition(protoObject);

/**
 * Inicialização do cliente
 */
const client = new noteDefinition.NoteService('localhost:50051', grpc.credentials.createInsecure());

/**
 * rpc List (Void) returns (NoteListResponse);
 */
const list = () =>
    new Promise((resolve, reject) => {
        client.List({}, (error, { notes }) => {
            if (error) {
                reject(error);
            } else {
                resolve(notes);
            }
        });
    });

/**
 * rpc GetById (NoteGetByIdRequest) returns (Note);
 */
const getById = (id) =>
    new Promise((resolve, reject) => {
        client.GetById({ id }, (error, note) => {
            if (error) {
                reject(error);
            } else {
                resolve(note);
            }
        });
    });

/**
 * rpc Create (NoteCreateRequest) returns (Note);
 */
const create = (content) =>
    new Promise((resolve, reject) => {
        client.Create({ content }, (error, note) => {
            if (error) {
                reject(error);
            } else {
                resolve(note);
            }
        });
    });

/**
 * rpc DeleteById (NoteDeleteByIdRequest) returns (Void);
 */
const deleteById = (id) =>
    new Promise((resolve, reject) => {
        client.DeleteById({ id }, (error, note) => {
            if (error) {
                reject(error);
            } else {
                resolve(note);
            }
        });
    });

export default { list, getById, create, deleteById };
