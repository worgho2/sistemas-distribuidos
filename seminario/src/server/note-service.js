import grpc from '@grpc/grpc-js';
import protoLoader from '@grpc/proto-loader';
import path from 'node:path';
import { fileURLToPath } from 'node:url';
import * as noteRepository from './note-repository.js';

/**
 * Carregamento do proto dinamicamente
 */
const __dirname = path.dirname(fileURLToPath(import.meta.url));
const protoObject = protoLoader.loadSync(
    path.resolve(__dirname, '../../proto/note-service.proto')
);
const noteDefinition = grpc.loadPackageDefinition(protoObject);

/**
 * rpc List (Void) returns (NoteListResponse);
 */
function List(_, callback) {
    try {
        const notes = noteRepository.list();
        return callback(null, { notes });
    } catch (error) {
        return callback(error, null);
    }
}

/**
 * rpc GetById (NoteGetByIdRequest) returns (Note);
 */
function GetById({ request: { id } }, callback) {
    try {
        const note = noteRepository.getById(id);
        if (!note) throw new Error(`Note (${id}) not found`);
        return callback(null, note);
    } catch (error) {
        return callback(error, null);
    }
}

/**
 * rpc Create (NoteCreateRequest) returns (Note);
 */
function Create({ request: { content } }, callback) {
    try {
        const note = noteRepository.create(content);
        return callback(null, note);
    } catch (error) {
        return callback(error, null);
    }
}

/**
 * rpc DeleteById (NoteDeleteByIdRequest) returns (Void);
 */
function DeleteById({ request: { id } }, callback) {
    try {
        const note = noteRepository.deleteById(id);
        if (!note) throw new Error(`Note (${id}) does not exist`);
        return callback(null, note);
    } catch (error) {
        return callback(error, null);
    }
}

export default [
    noteDefinition.NoteService.service,
    { List, GetById, Create, DeleteById }
];
