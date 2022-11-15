import inquirer from 'inquirer';
import noteService from './note-service.js';

while (true) {
    console.log();

    await inquirer
        .prompt([
            {
                message: 'Notes Service',
                type: 'list',
                name: 'action',
                choices: ['List', 'Get', 'Create', 'Delete'],
            },
            {
                message: 'Note id',
                type: 'number',
                name: 'noteId',
                when(answers) {
                    return ['Get', 'Delete'].includes(answers.action);
                },
            },
            {
                message: 'Content',
                type: 'input',
                name: 'content',
                when(answers) {
                    return ['Create'].includes(answers.action);
                },
            },
        ])
        .then(async (answers) => {
            switch (answers.action) {
                case 'List': {
                    const notes = await noteService.list();
                    console.log(JSON.stringify(notes ?? [], null, 2));
                    break;
                }

                case 'Get': {
                    const note = await noteService.getById(answers.noteId);
                    console.log(JSON.stringify(note, null, 2));
                    break;
                }

                case 'Create': {
                    const note = await noteService.create(answers.content);
                    console.log(`Created note ${JSON.stringify(note)}`);
                    break;
                }

                case 'Delete': {
                    const note = await noteService.deleteById(answers.noteId);
                    console.log(`Delete note ${JSON.stringify(note)}`);
                    break;
                }
            }
        })
        .catch((error) => console.log(`Error: ${error.details ?? 'Unkown'}`));
}
