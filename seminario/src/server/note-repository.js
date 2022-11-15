let notes = [
    {
        id: 0,
        content: 'First note',
    },
];

let nextId = 1;

export function list() {
    return notes;
}

export function getById(id) {
    return notes.find((note) => note.id === id);
}

export function create(content) {
    const note = { id: nextId, content };
    nextId += 1;
    notes.push(note);
    return note;
}

export function deleteById(id) {
    const note = notes.find((note) => note.id === id);
    notes = notes.filter((note) => note.id !== id);
    return note;
}
