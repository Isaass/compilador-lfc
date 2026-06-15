.386
.model flat, stdcall
.stack 4096

extern _print_integer:PROC
extern _print_string:PROC
extern _read_integer:PROC
extern _read_string:PROC

.data
    idade dw 0
    ativo db 0
    t2 dw 0
    t3 dw 0
    t4 dw 0
    str0 db "Maior de idade", 0
    str1 db "Menor de idade", 0

.code
main PROC
    mov ax, 24
    mov word ptr [idade], ax
    mov ax, word ptr [idade]
    mov bx, 18
    cmp ax, bx
    setge al
    xor ah, ah
    mov word ptr [t2], ax
    mov ax, word ptr [t2]
    mov byte ptr [ativo], al
    mov al, byte ptr [ativo]
    xor ah, ah
    cmp ax, 0
    je L0
    push offset str0
    call _print_string
    jmp L1
L0:
    push offset str1
    call _print_string
L1:
L2:
    mov ax, word ptr [idade]
    mov bx, 25
    cmp ax, bx
    setl al
    xor ah, ah
    mov word ptr [t3], ax
    mov ax, word ptr [t3]
    cmp ax, 0
    je L3
    mov ax, word ptr [idade]
    mov bx, 1
    add ax, bx
    mov word ptr [t4], ax
    mov ax, word ptr [t4]
    mov word ptr [idade], ax
    jmp L2
L3:
    ret
main ENDP
END main
