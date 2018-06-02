/*
 * [YOUR NAME]
 * [DATE LAST MODIFIED]
 *
 * [DESCRIPTION OF THE PROGRAM]
 */
#include <stdio.h>
#include <string.h> // For strlen
#include <stdlib.h> // For malloc, free

/*
 * Process a single line of the file
 *
 * Parameters:
 *   line: Line to process from the file
 *   username: Username from the line (pass-by-reference)
 *   hash_id: Hash method id (pass-by-reference) [0=DES, 1=MD5, 5=SHA-256, 6=SHA-512]
 *   hash_salt: Salt used for this hash without id (pass-by-reference)
 *   hash_pwd: The hashed password without salt or id (pass-by-reference)
 *
 * Returns:
 *   -1 if line is not a valid username/password line
 *   0 on success
 */
int process_line(char *line, char **username, int *hash_id, char **hash_salt, char **hash_pwd );
