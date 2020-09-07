
/****** Object:  StoredProcedure [dbo].[GET_preference]    Script Date: 18/12/2019 12:45:22 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

	CREATE PROC [dbo].[GET_preference]

	@tx_action_name		varchar(32)		= NULL

	, @is_active			int						= 1
	, @id_env_key			int						= NULL
	, @id_user_mod_key		int						= NULL
	, @dtt_mod				datetime				= NULL

	, @id_state_key			int						= NULL
	, @id_action_key		int						= NULL
	, @dtt_valid_from		datetime				= NULL
	, @dtt_valid_to			datetime				= NULL

	, @dtt_as_at			datetime				= NULL
	, @dtt_last_refresh		datetime				= NULL

	, @id_user_key					int							= NULL
	, @tx_login_name				varchar (32)		= NULL
	, @id_group_key					int							= NULL
	, @tx_group_name				varchar (64)		= NULL

	, @tx_pref_group				varchar (64)		= NULL
	, @tx_pref_name					varchar (128)		= NULL
	, @tx_pref_value				varchar (512)		= NULL		OUTPUT

	, @id_request_key				int							= NULL 		OUTPUT

	, @is_sel_inactive_pref			bit							= 0
	, @is_sel_result				bit							= 0
	, @is_delete_rec				bit							= 1

	 , @is_sel_data			int						= NULL
	, @tx_log_level			varchar(32)			= NULL
	, @id_log_level			int						= NULL
	, @is_record_time		bit						= NULL
	, @is_print_msg			bit						= NULL
	, @is_persist_msg		bit						= NULL
	, @tx_json_log_msg		varchar(MAX)	= NULL		OUTPUT

AS
BEGIN
	-- GLOBAL VARS --
	DECLARE	  @g_tx_action_name_orig	varchar(32)
			, @g_tx_state_name_orig		varchar(64)

			, @g_id_env_key				int
			, @g_tx_env_name			varchar(256)

			, @g_ct_row					int
			, @g_id_return_status		int

			, @g_id_error_key			int
			, @g_tx_err_msg				varchar(1024)
			, @g_tx_err_msg_tmp			varchar(1024)
			, @g_is_outer_sp			bit

	DECLARE   @g_dt_sys_today			date
			, @g_dt_sys_prev_day		date
			, @g_dt_sys_next_day		date
			, @g_dt_sys_prev_biz_day	date
			, @g_dt_sys_next_biz_day	date

	

	--	DECLARE @g_in_tran		int
	DECLARE	@g_is_ext_txn	bit
	DECLARE @g_is_sp_txn	bit

	SELECT @g_is_ext_txn = 0, @g_is_sp_txn = 0

	DECLARE @g_tmp_int		int

	DECLARE   @g_dtt_log			datetime
	 		, @g_tx_tmp_log_msg		varchar(MAX)
	 		, @g_tx_log_msg			varchar(MAX)

		  	, @g_id_err_num			int
			, @g_id_err_sev			int
			, @g_id_err_state		int
			, @g_tx_sp_name			varchar(128)
			, @g_id_line_num		int
			, @g_id_log_level		int

			, @g_is_record_time		bit
			, @g_is_print_msg		bit
			, @g_is_persist_msg  	bit

			, @g_tx_json_log_msg	varchar(MAX)

	SELECT	@g_tx_json_log_msg = ''

	DECLARE @g_dtt_proc_start		datetime
	DECLARE @g_dtt_proc_end			datetime
	DECLARE @g_dtt_tot_elapsed		int

	DECLARE	@g_dtt_query_start		datetime
	DECLARE	@g_dtt_query_end		datetime
	DECLARE @g_dtt_query_elapsed	int

	DECLARE @g_tmp_tx_tot_time		varchar(255)
	DECLARE @g_tmp_tx_query_time	varchar(255)

	IF ( (@@NESTLEVEL = 1) )
	BEGIN
		IF ( ((@@OPTIONS & 2) != 2) AND (@@TRANCOUNT = 0) )
		BEGIN
			SELECT @g_is_ext_txn = 0
		END
		ELSE
		BEGIN
			SELECT @g_is_ext_txn = 1
		END
	END

	IF (((@tx_log_level IS NULL) OR (@tx_log_level = '?')) OR ((@is_persist_msg IS NULL) OR (@is_persist_msg = -2147483648)) OR ((@is_record_time IS NULL) OR (@is_record_time = -2147483648)))
	BEGIN
		EXEC @g_id_return_status = GET_sp_log_level @tx_sp_name='GET_preference', @tx_log_level=@tx_log_level OUTPUT, @id_log_level=@id_log_level OUTPUT, @is_record_time=@is_record_time OUTPUT, @is_print_msg=@is_print_msg OUTPUT, @is_persist_msg=@is_persist_msg OUTPUT
	END

	SELECT @g_is_record_time = @is_record_time, @g_is_print_msg = @is_print_msg, @g_is_persist_msg	= @is_persist_msg, @g_id_log_level = @id_log_level

	SELECT @g_tx_err_msg = 'Entering[GET_preference] : @tx_action[' + ISNULL(@tx_action_name, '?') + ']'
	SELECT @g_dtt_log = GETDATE(), @g_tx_sp_name = 'GET_preference.sp', @g_id_line_num = 144

	-- TODO_H : [Naz] Move to Function

	IF ( NOT (((@g_id_log_level IS NULL) OR (@g_id_log_level = -2147483648))) )
	BEGIN
		IF (2 >= @g_id_log_level)
		BEGIN
			IF (@g_is_print_msg = 1)
			BEGIN
				SELECT @g_tx_tmp_log_msg = '[' + @g_tx_sp_name + ':' + CONVERT(varchar, @g_id_line_num) + '] | ' + CONVERT(varchar, @g_dtt_log) + ' | ' + 'INFO ' + ' | ' + '?' + ' | [' + CONVERT(varchar, 0) + '] -> ' + ISNULL(@g_tx_err_msg, '?')
				PRINT  @g_tx_tmp_log_msg
			END

			IF ( (@g_is_persist_msg = 1) )
			BEGIN
				EXEC @g_id_return_status = ACT_msg_log   @tx_action_name = 'NEW', @id_env_key = @g_id_env_key, @id_user_mod_key = @id_user_mod_key, @dtt_mod = @g_dtt_log
													   , @id_state_key = @id_state_key, @id_action_key = @id_action_key, @tx_log_msg_type='?', @tx_log_level='INFO ', @id_log_level=2
													   , @tx_sp_name=@g_tx_sp_name, @id_line_num=@g_id_line_num, @id_err_code=0, @tx_log_msg=@g_tx_err_msg, @tx_json_log_msg = @tx_json_log_msg OUTPUT
			
				IF (@g_id_return_status != 0)
				BEGIN
					RAISERROR ('Error calling ACT_msg_log', 16, 1)
					IF (@g_is_sp_txn = 1)
					BEGIN
						SELECT @g_is_sp_txn = 0
						ROLLBACK TRANSACTION
				
						IF ( (@@NESTLEVEL = 1) )
						BEGIN
							EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
					
							IF (@g_id_return_status != 0)
							BEGIN
								PRINT 'ERROR SAVING JSON LOG MSG!'
							END
						END
					END
					IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
					BEGIN
						-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
				
						IF (30008 = 0)
						BEGIN
							--RAISERROR (@tx_json_log_msg, 10, 1)
							EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
				
						END
						ELSE
						BEGIN
							--RAISERROR (@tx_json_log_msg, 16, 1)
							EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
						END
					END
				
					RETURN 30008
				END
			END
		END
	END

	EXEC @g_id_return_status = GET_environment @id_env_key = @g_id_env_key OUTPUT, @tx_env_name	= @g_tx_env_name OUTPUT, @id_user_key = @id_user_mod_key

	SELECT @g_tx_err_msg = 'Error calling SP -> [GET_ENVIRONMENT] '

	IF (@g_id_return_status != 0)
	BEGIN
		RAISERROR (@g_tx_err_msg, 16, 1)
		IF (@g_is_sp_txn = 1)
		BEGIN
			SELECT @g_is_sp_txn = 0
			ROLLBACK TRANSACTION
	
			IF ( (@@NESTLEVEL = 1) )
			BEGIN
				EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
		
				IF (@g_id_return_status != 0)
				BEGIN
					PRINT 'ERROR SAVING JSON LOG MSG!'
				END
			END
		END
		IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
		BEGIN
			-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
	
			IF (30001 = 0)
			BEGIN
				--RAISERROR (@tx_json_log_msg, 10, 1)
				EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
	
			END
			ELSE
			BEGIN
				--RAISERROR (@tx_json_log_msg, 16, 1)
				EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
			END
		END
	
		RETURN 30001
	END

	IF ( ((@dtt_last_refresh IS NULL) OR (@dtt_last_refresh = '01/01/1970')) )
	BEGIN
		SELECT @dtt_last_refresh = '01/01/1970'
	END

	--declare local variables
	DECLARE   @l_id_region_key			int
	 		, @l_id_group_key			int
		--	, @l_int_row_count_user		int

	-- negate sel_inactv
	SELECT @is_sel_inactive_pref = ~@is_sel_inactive_pref

	-- GET user name

	EXEC @g_id_return_status = GET_system_key @id_env_key	= @g_id_env_key, @tx_key_name = 'id_request_key', @id_key_value	= @id_request_key OUTPUT, @num_keys = 1

	SELECT @g_tx_err_msg = 'Error generating key for id_request_key'
	IF (@g_id_return_status != 0)
	BEGIN
		RAISERROR (@g_tx_err_msg, 16, 1)
		IF (@g_is_sp_txn = 1)
		BEGIN
			SELECT @g_is_sp_txn = 0
			ROLLBACK TRANSACTION
	
			IF ( (@@NESTLEVEL = 1) )
			BEGIN
				EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
		
				IF (@g_id_return_status != 0)
				BEGIN
					PRINT 'ERROR SAVING JSON LOG MSG!'
				END
			END
		END
		IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
		BEGIN
			-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
	
			IF (30003 = 0)
			BEGIN
				--RAISERROR (@tx_json_log_msg, 10, 1)
				EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
	
			END
			ELSE
			BEGIN
				--RAISERROR (@tx_json_log_msg, 16, 1)
				EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
			END
		END
	
		RETURN 30003
	END

	--Get the group key ,user group key ,region key for the given user
	SELECT	  @id_user_key 		= USR.id_user_key
			, @tx_login_name	= USR.tx_login_name
			, @l_id_region_key	= USR.id_region_key
			, @l_id_group_key	= USR.id_group_key
	FROM	T_USER		USR
	JOIN	T_GROUP		GRP		ON GRP.id_group_key = USR.id_group_key
	WHERE	USR.id_user_key			= ISNULL(@id_user_key	, USR.id_user_key)
	AND		USR.tx_login_name		= ISNULL(@tx_login_name	, USR.tx_login_name)
	-- AND		USR.id_group_key		= ISNULL(@id_group_key	, USR.id_group_key)
	-- AND		GRP.tx_group_name		= ISNULL(@tx_group_name	, GRP.tx_group_name)
	AND		USR.is_active			= 1
	AND		GRP.is_active			= 1

	SELECT @g_ct_row = @@rowcount, @g_id_error_key = @@error
	SELECT @g_tx_log_msg = 'Error selecting from T_USER/T_GROUP for ' + CONVERT(varchar, ISNULL(@id_user_key, -1)) + ' ' + CONVERT(varchar, ISNULL(@tx_login_name, -1))
	IF ( (@g_ct_row = 0) OR (@g_id_error_key != 0) )
	BEGIN
		RAISERROR (@g_tx_log_msg, 16, 1)
		IF (@g_is_sp_txn = 1)
		BEGIN
			SELECT @g_is_sp_txn = 0
			ROLLBACK TRANSACTION
	
			IF ( (@@NESTLEVEL = 1) )
			BEGIN
				EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
		
				IF (@g_id_return_status != 0)
				BEGIN
					PRINT 'ERROR SAVING JSON LOG MSG!'
				END
			END
		END
		IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
		BEGIN
			-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
	
			IF (99999 = 0)
			BEGIN
				--RAISERROR (@tx_json_log_msg, 10, 1)
				EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
	
			END
			ELSE
			BEGIN
				--RAISERROR (@tx_json_log_msg, 16, 1)
				EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
			END
		END
	
		RETURN 99999
	END

	-- GLOBAL, REGIONAL, GROUP, USER

	--------------
	--- GLOBAL ---
	--------------

		SELECT @g_dtt_log = GETDATE(), @g_tx_sp_name = 'GET_preference.sp', @g_id_line_num = 363
	
		-- TODO_H : [Naz] Move to Function
	
		IF ( NOT (((@g_id_log_level IS NULL) OR (@g_id_log_level = -2147483648))) )
		BEGIN
			IF (1 >= @g_id_log_level)
			BEGIN
				IF (@g_is_print_msg = 1)
				BEGIN
					SELECT @g_tx_tmp_log_msg = '[' + @g_tx_sp_name + ':' + CONVERT(varchar, @g_id_line_num) + '] | ' + CONVERT(varchar, @g_dtt_log) + ' | ' + 'DEBUG' + ' | ' + '?' + ' | [' + CONVERT(varchar, 0) + '] -> ' + ISNULL('GLOBAL PREF', '?')
					PRINT  @g_tx_tmp_log_msg
				END
	
				IF ( (@g_is_persist_msg = 1) )
				BEGIN
					EXEC @g_id_return_status = ACT_msg_log   @tx_action_name = 'NEW', @id_env_key = @g_id_env_key, @id_user_mod_key = @id_user_mod_key, @dtt_mod = @g_dtt_log
														   , @id_state_key = @id_state_key, @id_action_key = @id_action_key, @tx_log_msg_type='?', @tx_log_level='DEBUG', @id_log_level=1
														   , @tx_sp_name=@g_tx_sp_name, @id_line_num=@g_id_line_num, @id_err_code=0, @tx_log_msg='GLOBAL PREF', @tx_json_log_msg = @tx_json_log_msg OUTPUT
				
					IF (@g_id_return_status != 0)
					BEGIN
						RAISERROR ('Error calling ACT_msg_log', 16, 1)
						IF (@g_is_sp_txn = 1)
						BEGIN
							SELECT @g_is_sp_txn = 0
							ROLLBACK TRANSACTION
					
							IF ( (@@NESTLEVEL = 1) )
							BEGIN
								EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
						
								IF (@g_id_return_status != 0)
								BEGIN
									PRINT 'ERROR SAVING JSON LOG MSG!'
								END
							END
						END
						IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
						BEGIN
							-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
					
							IF (30008 = 0)
							BEGIN
								--RAISERROR (@tx_json_log_msg, 10, 1)
								EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
					
							END
							ELSE
							BEGIN
								--RAISERROR (@tx_json_log_msg, 16, 1)
								EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
							END
						END
					
						RETURN 30008
					END
				END
			END
		END

		INSERT	INTO T_TMP_PREFERENCE
		SELECT	  @id_request_key
				, PREF.id_pref_key
				, PREF.id_pref_ver
				, @id_user_key
				, GETDATE()

				, PREF.tx_type_category
				, PREF.tx_type_name
				, PREF.id_pref_type_value_key
				, PREF.tx_pref_type_value
				, PREF.id_pref_type_value_key_value
				, PREF.tx_pref_group
				, PREF.tx_pref_name
				, PREF.tx_pref_value
				, PREF.is_allow_override
				, PREF.int_sort_order
				, PREF.int_pref_order
				, PREF.tx_pref_desc

		FROM	V_PREFERENCE PREF
		WHERE	PREF.tx_pref_type_value	= 'GLOBAL'
		AND		PREF.tx_pref_group		= ISNULL(@tx_pref_group, PREF.tx_pref_group)
		AND		PREF.is_active			IN (1, @is_sel_inactive_pref)

		SELECT @g_ct_row = @@rowcount, @g_id_error_key = @@error
		SELECT @g_tx_err_msg = 'Error Selecting GLOBAL_PREF'
		IF (@g_id_error_key != 0)
		BEGIN
			RAISERROR (@g_tx_err_msg, 16, 1)
			IF (@g_is_sp_txn = 1)
		BEGIN
			SELECT @g_is_sp_txn = 0
			ROLLBACK TRANSACTION
	
			IF ( (@@NESTLEVEL = 1) )
			BEGIN
				EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
		
				IF (@g_id_return_status != 0)
				BEGIN
					PRINT 'ERROR SAVING JSON LOG MSG!'
				END
			END
		END
			IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
			BEGIN
				-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
		
				IF (99999 = 0)
				BEGIN
					--RAISERROR (@tx_json_log_msg, 10, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
		
				END
				ELSE
				BEGIN
					--RAISERROR (@tx_json_log_msg, 16, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
				END
			END
		
			RETURN 99999
		END

		SELECT @g_tx_log_msg = 'GLOBAL PREF COUNT : ' + CONVERT(varchar, @g_ct_row)
		SELECT @g_dtt_log = GETDATE(), @g_tx_sp_name = 'GET_preference.sp', @g_id_line_num = 490
	
		-- TODO_H : [Naz] Move to Function
	
		IF ( NOT (((@g_id_log_level IS NULL) OR (@g_id_log_level = -2147483648))) )
		BEGIN
			IF (1 >= @g_id_log_level)
			BEGIN
				IF (@g_is_print_msg = 1)
				BEGIN
					SELECT @g_tx_tmp_log_msg = '[' + @g_tx_sp_name + ':' + CONVERT(varchar, @g_id_line_num) + '] | ' + CONVERT(varchar, @g_dtt_log) + ' | ' + 'DEBUG' + ' | ' + '?' + ' | [' + CONVERT(varchar, 0) + '] -> ' + ISNULL(@g_tx_log_msg, '?')
					PRINT  @g_tx_tmp_log_msg
				END
	
				IF ( (@g_is_persist_msg = 1) )
				BEGIN
					EXEC @g_id_return_status = ACT_msg_log   @tx_action_name = 'NEW', @id_env_key = @g_id_env_key, @id_user_mod_key = @id_user_mod_key, @dtt_mod = @g_dtt_log
														   , @id_state_key = @id_state_key, @id_action_key = @id_action_key, @tx_log_msg_type='?', @tx_log_level='DEBUG', @id_log_level=1
														   , @tx_sp_name=@g_tx_sp_name, @id_line_num=@g_id_line_num, @id_err_code=0, @tx_log_msg=@g_tx_log_msg, @tx_json_log_msg = @tx_json_log_msg OUTPUT
				
					IF (@g_id_return_status != 0)
					BEGIN
						RAISERROR ('Error calling ACT_msg_log', 16, 1)
						IF (@g_is_sp_txn = 1)
						BEGIN
							SELECT @g_is_sp_txn = 0
							ROLLBACK TRANSACTION
					
							IF ( (@@NESTLEVEL = 1) )
							BEGIN
								EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
						
								IF (@g_id_return_status != 0)
								BEGIN
									PRINT 'ERROR SAVING JSON LOG MSG!'
								END
							END
						END
						IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
						BEGIN
							-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
					
							IF (30008 = 0)
							BEGIN
								--RAISERROR (@tx_json_log_msg, 10, 1)
								EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
					
							END
							ELSE
							BEGIN
								--RAISERROR (@tx_json_log_msg, 16, 1)
								EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
							END
						END
					
						RETURN 30008
					END
				END
			END
		END


	----------------
	--- Regional ---
	----------------
		-- Update followed by insert

		SELECT @g_dtt_log = GETDATE(), @g_tx_sp_name = 'GET_preference.sp', @g_id_line_num = 557
	
		-- TODO_H : [Naz] Move to Function
	
		IF ( NOT (((@g_id_log_level IS NULL) OR (@g_id_log_level = -2147483648))) )
		BEGIN
			IF (1 >= @g_id_log_level)
			BEGIN
				IF (@g_is_print_msg = 1)
				BEGIN
					SELECT @g_tx_tmp_log_msg = '[' + @g_tx_sp_name + ':' + CONVERT(varchar, @g_id_line_num) + '] | ' + CONVERT(varchar, @g_dtt_log) + ' | ' + 'DEBUG' + ' | ' + '?' + ' | [' + CONVERT(varchar, 0) + '] -> ' + ISNULL('REGIONAL PREF - UPDATE', '?')
					PRINT  @g_tx_tmp_log_msg
				END
	
				IF ( (@g_is_persist_msg = 1) )
				BEGIN
					EXEC @g_id_return_status = ACT_msg_log   @tx_action_name = 'NEW', @id_env_key = @g_id_env_key, @id_user_mod_key = @id_user_mod_key, @dtt_mod = @g_dtt_log
														   , @id_state_key = @id_state_key, @id_action_key = @id_action_key, @tx_log_msg_type='?', @tx_log_level='DEBUG', @id_log_level=1
														   , @tx_sp_name=@g_tx_sp_name, @id_line_num=@g_id_line_num, @id_err_code=0, @tx_log_msg='REGIONAL PREF - UPDATE', @tx_json_log_msg = @tx_json_log_msg OUTPUT
				
					IF (@g_id_return_status != 0)
					BEGIN
						RAISERROR ('Error calling ACT_msg_log', 16, 1)
						IF (@g_is_sp_txn = 1)
						BEGIN
							SELECT @g_is_sp_txn = 0
							ROLLBACK TRANSACTION
					
							IF ( (@@NESTLEVEL = 1) )
							BEGIN
								EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
						
								IF (@g_id_return_status != 0)
								BEGIN
									PRINT 'ERROR SAVING JSON LOG MSG!'
								END
							END
						END
						IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
						BEGIN
							-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
					
							IF (30008 = 0)
							BEGIN
								--RAISERROR (@tx_json_log_msg, 10, 1)
								EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
					
							END
							ELSE
							BEGIN
								--RAISERROR (@tx_json_log_msg, 16, 1)
								EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
							END
						END
					
						RETURN 30008
					END
				END
			END
		END

		UPDATE	T_TMP_PREFERENCE
		SET		  id_pref_key						= PREF.id_pref_key
				, id_pref_ver						= PREF.id_pref_ver
				, tx_type_category					= PREF.tx_type_category
				, tx_type_name						= PREF.tx_type_name
				, id_pref_type_value_key			= PREF.id_pref_type_value_key
				, tx_pref_type_value				= PREF.tx_pref_type_value
				, id_pref_type_value_key_value		= PREF.id_pref_type_value_key_value
				, tx_pref_group						= PREF.tx_pref_group
				, tx_pref_name						= PREF.tx_pref_name
				, tx_pref_value						= PREF.tx_pref_value
				, is_allow_override					= PREF.is_allow_override
				, int_sort_order					= PREF.int_sort_order
				, int_pref_order					= PREF.int_pref_order
				, tx_pref_desc						= PREF.tx_pref_desc

		FROM	V_PREFERENCE		PREF
		JOIN	T_TMP_PREFERENCE	TMP		ON 	TMP.tx_pref_name 		= PREF.tx_pref_name
												AND TMP.tx_pref_group 	= PREF.tx_pref_group
		WHERE	PREF.tx_pref_type_value				= 'REGIONAL'
		AND		PREF.id_pref_type_value_key_value	= @l_id_region_key
		AND		PREF.tx_pref_group					= ISNULL(@tx_pref_group, PREF.tx_pref_group)
		AND		TMP.is_allow_override				!= 0
		AND		PREF.is_active						IN (1, @is_sel_inactive_pref)

		SELECT @g_ct_row = @@rowcount, @g_id_error_key = @@error
		SELECT @g_tx_err_msg = 'Error UPDATING with REGIONAL preference!'
		IF (@g_id_error_key != 0)
		BEGIN
			RAISERROR (@g_tx_err_msg, 16, 1)
			IF (@g_is_sp_txn = 1)
		BEGIN
			SELECT @g_is_sp_txn = 0
			ROLLBACK TRANSACTION
	
			IF ( (@@NESTLEVEL = 1) )
			BEGIN
				EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
		
				IF (@g_id_return_status != 0)
				BEGIN
					PRINT 'ERROR SAVING JSON LOG MSG!'
				END
			END
		END
			IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
			BEGIN
				-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
		
				IF (99999 = 0)
				BEGIN
					--RAISERROR (@tx_json_log_msg, 10, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
		
				END
				ELSE
				BEGIN
					--RAISERROR (@tx_json_log_msg, 16, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
				END
			END
		
			RETURN 99999
		END

		SELECT @g_tx_log_msg = 'REGIONAL PREF UPD COUNT : ' + CONVERT(varchar, @g_ct_row)
		SELECT @g_dtt_log = GETDATE(), @g_tx_sp_name = 'GET_preference.sp', @g_id_line_num = 684
	
		-- TODO_H : [Naz] Move to Function
	
		IF ( NOT (((@g_id_log_level IS NULL) OR (@g_id_log_level = -2147483648))) )
		BEGIN
			IF (1 >= @g_id_log_level)
			BEGIN
				IF (@g_is_print_msg = 1)
				BEGIN
					SELECT @g_tx_tmp_log_msg = '[' + @g_tx_sp_name + ':' + CONVERT(varchar, @g_id_line_num) + '] | ' + CONVERT(varchar, @g_dtt_log) + ' | ' + 'DEBUG' + ' | ' + '?' + ' | [' + CONVERT(varchar, 0) + '] -> ' + ISNULL(@g_tx_log_msg, '?')
					PRINT  @g_tx_tmp_log_msg
				END
	
				IF ( (@g_is_persist_msg = 1) )
				BEGIN
					EXEC @g_id_return_status = ACT_msg_log   @tx_action_name = 'NEW', @id_env_key = @g_id_env_key, @id_user_mod_key = @id_user_mod_key, @dtt_mod = @g_dtt_log
														   , @id_state_key = @id_state_key, @id_action_key = @id_action_key, @tx_log_msg_type='?', @tx_log_level='DEBUG', @id_log_level=1
														   , @tx_sp_name=@g_tx_sp_name, @id_line_num=@g_id_line_num, @id_err_code=0, @tx_log_msg=@g_tx_log_msg, @tx_json_log_msg = @tx_json_log_msg OUTPUT
				
					IF (@g_id_return_status != 0)
					BEGIN
						RAISERROR ('Error calling ACT_msg_log', 16, 1)
						IF (@g_is_sp_txn = 1)
						BEGIN
							SELECT @g_is_sp_txn = 0
							ROLLBACK TRANSACTION
					
							IF ( (@@NESTLEVEL = 1) )
							BEGIN
								EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
						
								IF (@g_id_return_status != 0)
								BEGIN
									PRINT 'ERROR SAVING JSON LOG MSG!'
								END
							END
						END
						IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
						BEGIN
							-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
					
							IF (30008 = 0)
							BEGIN
								--RAISERROR (@tx_json_log_msg, 10, 1)
								EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
					
							END
							ELSE
							BEGIN
								--RAISERROR (@tx_json_log_msg, 16, 1)
								EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
							END
						END
					
						RETURN 30008
					END
				END
			END
		END

		SELECT @g_dtt_log = GETDATE(), @g_tx_sp_name = 'GET_preference.sp', @g_id_line_num = 745
	
		-- TODO_H : [Naz] Move to Function
	
		IF ( NOT (((@g_id_log_level IS NULL) OR (@g_id_log_level = -2147483648))) )
		BEGIN
			IF (1 >= @g_id_log_level)
			BEGIN
				IF (@g_is_print_msg = 1)
				BEGIN
					SELECT @g_tx_tmp_log_msg = '[' + @g_tx_sp_name + ':' + CONVERT(varchar, @g_id_line_num) + '] | ' + CONVERT(varchar, @g_dtt_log) + ' | ' + 'DEBUG' + ' | ' + '?' + ' | [' + CONVERT(varchar, 0) + '] -> ' + ISNULL('REGIONAL PREF - INSERT', '?')
					PRINT  @g_tx_tmp_log_msg
				END
	
				IF ( (@g_is_persist_msg = 1) )
				BEGIN
					EXEC @g_id_return_status = ACT_msg_log   @tx_action_name = 'NEW', @id_env_key = @g_id_env_key, @id_user_mod_key = @id_user_mod_key, @dtt_mod = @g_dtt_log
														   , @id_state_key = @id_state_key, @id_action_key = @id_action_key, @tx_log_msg_type='?', @tx_log_level='DEBUG', @id_log_level=1
														   , @tx_sp_name=@g_tx_sp_name, @id_line_num=@g_id_line_num, @id_err_code=0, @tx_log_msg='REGIONAL PREF - INSERT', @tx_json_log_msg = @tx_json_log_msg OUTPUT
				
					IF (@g_id_return_status != 0)
					BEGIN
						RAISERROR ('Error calling ACT_msg_log', 16, 1)
						IF (@g_is_sp_txn = 1)
						BEGIN
							SELECT @g_is_sp_txn = 0
							ROLLBACK TRANSACTION
					
							IF ( (@@NESTLEVEL = 1) )
							BEGIN
								EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
						
								IF (@g_id_return_status != 0)
								BEGIN
									PRINT 'ERROR SAVING JSON LOG MSG!'
								END
							END
						END
						IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
						BEGIN
							-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
					
							IF (30008 = 0)
							BEGIN
								--RAISERROR (@tx_json_log_msg, 10, 1)
								EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
					
							END
							ELSE
							BEGIN
								--RAISERROR (@tx_json_log_msg, 16, 1)
								EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
							END
						END
					
						RETURN 30008
					END
				END
			END
		END
		INSERT	INTO T_TMP_PREFERENCE
		SELECT	  @id_request_key
				, PREF.id_pref_key
				, PREF.id_pref_ver
				, @id_user_key
				, GETDATE()

				, PREF.tx_type_category
				, PREF.tx_type_name
				, PREF.id_pref_type_value_key
				, PREF.tx_pref_type_value
				, PREF.id_pref_type_value_key_value
				, PREF.tx_pref_group
				, PREF.tx_pref_name
				, PREF.tx_pref_value
				, PREF.is_allow_override
				, PREF.int_sort_order
				, PREF.int_pref_order
				, PREF.tx_pref_desc

		FROM	V_PREFERENCE PREF
		WHERE	PREF.tx_pref_type_value				= 'REGIONAL'
		AND		PREF.id_pref_type_value_key_value	= @l_id_region_key
		AND		PREF.tx_pref_group					= ISNULL(@tx_pref_group, PREF.tx_pref_group)
		AND		PREF.is_active						IN (1, @is_sel_inactive_pref)
		AND		PREF.tx_pref_name					NOT IN ( SELECT tx_pref_name	FROM T_TMP_PREFERENCE WHERE id_request_key = @id_request_key)

		SELECT @g_ct_row = @@rowcount, @g_id_error_key = @@error
		SELECT @g_tx_err_msg = 'Error INSERTING REGIONAL Preference!'
		IF (@g_id_error_key != 0)
		BEGIN
			RAISERROR (@g_tx_err_msg, 16, 1)
			IF (@g_is_sp_txn = 1)
		BEGIN
			SELECT @g_is_sp_txn = 0
			ROLLBACK TRANSACTION
	
			IF ( (@@NESTLEVEL = 1) )
			BEGIN
				EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
		
				IF (@g_id_return_status != 0)
				BEGIN
					PRINT 'ERROR SAVING JSON LOG MSG!'
				END
			END
		END
			IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
			BEGIN
				-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
		
				IF (99999 = 0)
				BEGIN
					--RAISERROR (@tx_json_log_msg, 10, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
		
				END
				ELSE
				BEGIN
					--RAISERROR (@tx_json_log_msg, 16, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
				END
			END
		
			RETURN 99999
		END

		SELECT @g_tx_log_msg = 'REGIONAL PREF INS COUNT : ' + CONVERT(varchar, @g_ct_row)
		SELECT @g_dtt_log = GETDATE(), @g_tx_sp_name = 'GET_preference.sp', @g_id_line_num = 873
	
		-- TODO_H : [Naz] Move to Function
	
		IF ( NOT (((@g_id_log_level IS NULL) OR (@g_id_log_level = -2147483648))) )
		BEGIN
			IF (1 >= @g_id_log_level)
			BEGIN
				IF (@g_is_print_msg = 1)
				BEGIN
					SELECT @g_tx_tmp_log_msg = '[' + @g_tx_sp_name + ':' + CONVERT(varchar, @g_id_line_num) + '] | ' + CONVERT(varchar, @g_dtt_log) + ' | ' + 'DEBUG' + ' | ' + '?' + ' | [' + CONVERT(varchar, 0) + '] -> ' + ISNULL(@g_tx_log_msg, '?')
					PRINT  @g_tx_tmp_log_msg
				END
	
				IF ( (@g_is_persist_msg = 1) )
				BEGIN
					EXEC @g_id_return_status = ACT_msg_log   @tx_action_name = 'NEW', @id_env_key = @g_id_env_key, @id_user_mod_key = @id_user_mod_key, @dtt_mod = @g_dtt_log
														   , @id_state_key = @id_state_key, @id_action_key = @id_action_key, @tx_log_msg_type='?', @tx_log_level='DEBUG', @id_log_level=1
														   , @tx_sp_name=@g_tx_sp_name, @id_line_num=@g_id_line_num, @id_err_code=0, @tx_log_msg=@g_tx_log_msg, @tx_json_log_msg = @tx_json_log_msg OUTPUT
				
					IF (@g_id_return_status != 0)
					BEGIN
						RAISERROR ('Error calling ACT_msg_log', 16, 1)
						IF (@g_is_sp_txn = 1)
						BEGIN
							SELECT @g_is_sp_txn = 0
							ROLLBACK TRANSACTION
					
							IF ( (@@NESTLEVEL = 1) )
							BEGIN
								EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
						
								IF (@g_id_return_status != 0)
								BEGIN
									PRINT 'ERROR SAVING JSON LOG MSG!'
								END
							END
						END
						IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
						BEGIN
							-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
					
							IF (30008 = 0)
							BEGIN
								--RAISERROR (@tx_json_log_msg, 10, 1)
								EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
					
							END
							ELSE
							BEGIN
								--RAISERROR (@tx_json_log_msg, 16, 1)
								EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
							END
						END
					
						RETURN 30008
					END
				END
			END
		END

	--------------
	--- GROUP ----
	--------------
		-- Update followed by insert

		SELECT @g_dtt_log = GETDATE(), @g_tx_sp_name = 'GET_preference.sp', @g_id_line_num = 939
	
		-- TODO_H : [Naz] Move to Function
	
		IF ( NOT (((@g_id_log_level IS NULL) OR (@g_id_log_level = -2147483648))) )
		BEGIN
			IF (1 >= @g_id_log_level)
			BEGIN
				IF (@g_is_print_msg = 1)
				BEGIN
					SELECT @g_tx_tmp_log_msg = '[' + @g_tx_sp_name + ':' + CONVERT(varchar, @g_id_line_num) + '] | ' + CONVERT(varchar, @g_dtt_log) + ' | ' + 'DEBUG' + ' | ' + '?' + ' | [' + CONVERT(varchar, 0) + '] -> ' + ISNULL('GROUP PREF - UPDATE', '?')
					PRINT  @g_tx_tmp_log_msg
				END
	
				IF ( (@g_is_persist_msg = 1) )
				BEGIN
					EXEC @g_id_return_status = ACT_msg_log   @tx_action_name = 'NEW', @id_env_key = @g_id_env_key, @id_user_mod_key = @id_user_mod_key, @dtt_mod = @g_dtt_log
														   , @id_state_key = @id_state_key, @id_action_key = @id_action_key, @tx_log_msg_type='?', @tx_log_level='DEBUG', @id_log_level=1
														   , @tx_sp_name=@g_tx_sp_name, @id_line_num=@g_id_line_num, @id_err_code=0, @tx_log_msg='GROUP PREF - UPDATE', @tx_json_log_msg = @tx_json_log_msg OUTPUT
				
					IF (@g_id_return_status != 0)
					BEGIN
						RAISERROR ('Error calling ACT_msg_log', 16, 1)
						IF (@g_is_sp_txn = 1)
						BEGIN
							SELECT @g_is_sp_txn = 0
							ROLLBACK TRANSACTION
					
							IF ( (@@NESTLEVEL = 1) )
							BEGIN
								EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
						
								IF (@g_id_return_status != 0)
								BEGIN
									PRINT 'ERROR SAVING JSON LOG MSG!'
								END
							END
						END
						IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
						BEGIN
							-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
					
							IF (30008 = 0)
							BEGIN
								--RAISERROR (@tx_json_log_msg, 10, 1)
								EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
					
							END
							ELSE
							BEGIN
								--RAISERROR (@tx_json_log_msg, 16, 1)
								EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
							END
						END
					
						RETURN 30008
					END
				END
			END
		END

		UPDATE	T_TMP_PREFERENCE
		SET		  id_pref_key						= PREF.id_pref_key
				, id_pref_ver						= PREF.id_pref_ver
				, tx_type_category					= PREF.tx_type_category
				, tx_type_name						= PREF.tx_type_name
				, id_pref_type_value_key			= PREF.id_pref_type_value_key
				, tx_pref_type_value				= PREF.tx_pref_type_value
				, id_pref_type_value_key_value		= PREF.id_pref_type_value_key_value
				, tx_pref_group						= PREF.tx_pref_group
				, tx_pref_name						= PREF.tx_pref_name
				, tx_pref_value						= PREF.tx_pref_value
				, is_allow_override					= PREF.is_allow_override
				, int_sort_order					= PREF.int_sort_order
				, int_pref_order					= PREF.int_pref_order
				, tx_pref_desc						= PREF.tx_pref_desc

		FROM	V_PREFERENCE		PREF
		JOIN	T_TMP_PREFERENCE	TMP		ON 	TMP.tx_pref_name 		= PREF.tx_pref_name
												AND TMP.tx_pref_group 	= PREF.tx_pref_group
		WHERE	PREF.tx_pref_type_value				= 'GROUP'
		AND		PREF.id_pref_type_value_key_value	= @l_id_group_key
		AND		PREF.tx_pref_group					= ISNULL(@tx_pref_group, PREF.tx_pref_group)
		AND		TMP.is_allow_override				!= 0
		AND		PREF.is_active						IN (1, @is_sel_inactive_pref)

		SELECT @g_ct_row = @@rowcount, @g_id_error_key = @@error
		SELECT @g_tx_err_msg = 'Error UPDATING with GROUP preference!'
		IF (@g_id_error_key != 0)
		BEGIN
			RAISERROR (@g_tx_err_msg, 16, 1)
			IF (@g_is_sp_txn = 1)
		BEGIN
			SELECT @g_is_sp_txn = 0
			ROLLBACK TRANSACTION
	
			IF ( (@@NESTLEVEL = 1) )
			BEGIN
				EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
		
				IF (@g_id_return_status != 0)
				BEGIN
					PRINT 'ERROR SAVING JSON LOG MSG!'
				END
			END
		END
			IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
			BEGIN
				-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
		
				IF (99999 = 0)
				BEGIN
					--RAISERROR (@tx_json_log_msg, 10, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
		
				END
				ELSE
				BEGIN
					--RAISERROR (@tx_json_log_msg, 16, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
				END
			END
		
			RETURN 99999
		END

		SELECT @g_tx_log_msg = 'GROUP PREF UPD COUNT : ' + CONVERT(varchar, @g_ct_row)
		SELECT @g_dtt_log = GETDATE(), @g_tx_sp_name = 'GET_preference.sp', @g_id_line_num = 1066
	
		-- TODO_H : [Naz] Move to Function
	
		IF ( NOT (((@g_id_log_level IS NULL) OR (@g_id_log_level = -2147483648))) )
		BEGIN
			IF (1 >= @g_id_log_level)
			BEGIN
				IF (@g_is_print_msg = 1)
				BEGIN
					SELECT @g_tx_tmp_log_msg = '[' + @g_tx_sp_name + ':' + CONVERT(varchar, @g_id_line_num) + '] | ' + CONVERT(varchar, @g_dtt_log) + ' | ' + 'DEBUG' + ' | ' + '?' + ' | [' + CONVERT(varchar, 0) + '] -> ' + ISNULL(@g_tx_log_msg, '?')
					PRINT  @g_tx_tmp_log_msg
				END
	
				IF ( (@g_is_persist_msg = 1) )
				BEGIN
					EXEC @g_id_return_status = ACT_msg_log   @tx_action_name = 'NEW', @id_env_key = @g_id_env_key, @id_user_mod_key = @id_user_mod_key, @dtt_mod = @g_dtt_log
														   , @id_state_key = @id_state_key, @id_action_key = @id_action_key, @tx_log_msg_type='?', @tx_log_level='DEBUG', @id_log_level=1
														   , @tx_sp_name=@g_tx_sp_name, @id_line_num=@g_id_line_num, @id_err_code=0, @tx_log_msg=@g_tx_log_msg, @tx_json_log_msg = @tx_json_log_msg OUTPUT
				
					IF (@g_id_return_status != 0)
					BEGIN
						RAISERROR ('Error calling ACT_msg_log', 16, 1)
						IF (@g_is_sp_txn = 1)
						BEGIN
							SELECT @g_is_sp_txn = 0
							ROLLBACK TRANSACTION
					
							IF ( (@@NESTLEVEL = 1) )
							BEGIN
								EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
						
								IF (@g_id_return_status != 0)
								BEGIN
									PRINT 'ERROR SAVING JSON LOG MSG!'
								END
							END
						END
						IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
						BEGIN
							-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
					
							IF (30008 = 0)
							BEGIN
								--RAISERROR (@tx_json_log_msg, 10, 1)
								EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
					
							END
							ELSE
							BEGIN
								--RAISERROR (@tx_json_log_msg, 16, 1)
								EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
							END
						END
					
						RETURN 30008
					END
				END
			END
		END


		SELECT @g_dtt_log = GETDATE(), @g_tx_sp_name = 'GET_preference.sp', @g_id_line_num = 1128
	
		-- TODO_H : [Naz] Move to Function
	
		IF ( NOT (((@g_id_log_level IS NULL) OR (@g_id_log_level = -2147483648))) )
		BEGIN
			IF (1 >= @g_id_log_level)
			BEGIN
				IF (@g_is_print_msg = 1)
				BEGIN
					SELECT @g_tx_tmp_log_msg = '[' + @g_tx_sp_name + ':' + CONVERT(varchar, @g_id_line_num) + '] | ' + CONVERT(varchar, @g_dtt_log) + ' | ' + 'DEBUG' + ' | ' + '?' + ' | [' + CONVERT(varchar, 0) + '] -> ' + ISNULL('GROUP PREF - INSERT', '?')
					PRINT  @g_tx_tmp_log_msg
				END
	
				IF ( (@g_is_persist_msg = 1) )
				BEGIN
					EXEC @g_id_return_status = ACT_msg_log   @tx_action_name = 'NEW', @id_env_key = @g_id_env_key, @id_user_mod_key = @id_user_mod_key, @dtt_mod = @g_dtt_log
														   , @id_state_key = @id_state_key, @id_action_key = @id_action_key, @tx_log_msg_type='?', @tx_log_level='DEBUG', @id_log_level=1
														   , @tx_sp_name=@g_tx_sp_name, @id_line_num=@g_id_line_num, @id_err_code=0, @tx_log_msg='GROUP PREF - INSERT', @tx_json_log_msg = @tx_json_log_msg OUTPUT
				
					IF (@g_id_return_status != 0)
					BEGIN
						RAISERROR ('Error calling ACT_msg_log', 16, 1)
						IF (@g_is_sp_txn = 1)
						BEGIN
							SELECT @g_is_sp_txn = 0
							ROLLBACK TRANSACTION
					
							IF ( (@@NESTLEVEL = 1) )
							BEGIN
								EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
						
								IF (@g_id_return_status != 0)
								BEGIN
									PRINT 'ERROR SAVING JSON LOG MSG!'
								END
							END
						END
						IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
						BEGIN
							-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
					
							IF (30008 = 0)
							BEGIN
								--RAISERROR (@tx_json_log_msg, 10, 1)
								EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
					
							END
							ELSE
							BEGIN
								--RAISERROR (@tx_json_log_msg, 16, 1)
								EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
							END
						END
					
						RETURN 30008
					END
				END
			END
		END

		INSERT	INTO T_TMP_PREFERENCE
		SELECT	  @id_request_key
				, PREF.id_pref_key
				, PREF.id_pref_ver
				, @id_user_key
				, GETDATE()

				, PREF.tx_type_category
				, PREF.tx_type_name
				, PREF.id_pref_type_value_key
				, PREF.tx_pref_type_value
				, PREF.id_pref_type_value_key_value
				, PREF.tx_pref_group
				, PREF.tx_pref_name
				, PREF.tx_pref_value
				, PREF.is_allow_override
				, PREF.int_sort_order
				, PREF.int_pref_order
				, PREF.tx_pref_desc

		FROM	V_PREFERENCE PREF
		WHERE	PREF.tx_pref_type_value				= 'GROUP'
		AND		PREF.id_pref_type_value_key_value	= @l_id_group_key
		AND		PREF.tx_pref_group					= ISNULL(@tx_pref_group, PREF.tx_pref_group)
		AND		PREF.is_active						IN (1, @is_sel_inactive_pref)
		AND		PREF.tx_pref_name					NOT IN ( SELECT tx_pref_name	FROM T_TMP_PREFERENCE WHERE id_request_key = @id_request_key)

		SELECT @g_ct_row = @@rowcount, @g_id_error_key = @@error
		SELECT @g_tx_err_msg = 'Error INSERTING GROUP Preference!'
		IF (@g_id_error_key != 0)
		BEGIN
			RAISERROR (@g_tx_err_msg, 16, 1)
			IF (@g_is_sp_txn = 1)
		BEGIN
			SELECT @g_is_sp_txn = 0
			ROLLBACK TRANSACTION
	
			IF ( (@@NESTLEVEL = 1) )
			BEGIN
				EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
		
				IF (@g_id_return_status != 0)
				BEGIN
					PRINT 'ERROR SAVING JSON LOG MSG!'
				END
			END
		END
			IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
			BEGIN
				-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
		
				IF (99999 = 0)
				BEGIN
					--RAISERROR (@tx_json_log_msg, 10, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
		
				END
				ELSE
				BEGIN
					--RAISERROR (@tx_json_log_msg, 16, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
				END
			END
		
			RETURN 99999
		END

		SELECT @g_tx_log_msg = 'GROUP PREF INS COUNT : ' + CONVERT(varchar, @g_ct_row)
		SELECT @g_dtt_log = GETDATE(), @g_tx_sp_name = 'GET_preference.sp', @g_id_line_num = 1257
	
		-- TODO_H : [Naz] Move to Function
	
		IF ( NOT (((@g_id_log_level IS NULL) OR (@g_id_log_level = -2147483648))) )
		BEGIN
			IF (1 >= @g_id_log_level)
			BEGIN
				IF (@g_is_print_msg = 1)
				BEGIN
					SELECT @g_tx_tmp_log_msg = '[' + @g_tx_sp_name + ':' + CONVERT(varchar, @g_id_line_num) + '] | ' + CONVERT(varchar, @g_dtt_log) + ' | ' + 'DEBUG' + ' | ' + '?' + ' | [' + CONVERT(varchar, 0) + '] -> ' + ISNULL(@g_tx_log_msg, '?')
					PRINT  @g_tx_tmp_log_msg
				END
	
				IF ( (@g_is_persist_msg = 1) )
				BEGIN
					EXEC @g_id_return_status = ACT_msg_log   @tx_action_name = 'NEW', @id_env_key = @g_id_env_key, @id_user_mod_key = @id_user_mod_key, @dtt_mod = @g_dtt_log
														   , @id_state_key = @id_state_key, @id_action_key = @id_action_key, @tx_log_msg_type='?', @tx_log_level='DEBUG', @id_log_level=1
														   , @tx_sp_name=@g_tx_sp_name, @id_line_num=@g_id_line_num, @id_err_code=0, @tx_log_msg=@g_tx_log_msg, @tx_json_log_msg = @tx_json_log_msg OUTPUT
				
					IF (@g_id_return_status != 0)
					BEGIN
						RAISERROR ('Error calling ACT_msg_log', 16, 1)
						IF (@g_is_sp_txn = 1)
						BEGIN
							SELECT @g_is_sp_txn = 0
							ROLLBACK TRANSACTION
					
							IF ( (@@NESTLEVEL = 1) )
							BEGIN
								EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
						
								IF (@g_id_return_status != 0)
								BEGIN
									PRINT 'ERROR SAVING JSON LOG MSG!'
								END
							END
						END
						IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
						BEGIN
							-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
					
							IF (30008 = 0)
							BEGIN
								--RAISERROR (@tx_json_log_msg, 10, 1)
								EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
					
							END
							ELSE
							BEGIN
								--RAISERROR (@tx_json_log_msg, 16, 1)
								EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
							END
						END
					
						RETURN 30008
					END
				END
			END
		END

	-------------
	--- USER ----
	-------------
		-- Update followed by insert

		SELECT @g_dtt_log = GETDATE(), @g_tx_sp_name = 'GET_preference.sp', @g_id_line_num = 1323
	
		-- TODO_H : [Naz] Move to Function
	
		IF ( NOT (((@g_id_log_level IS NULL) OR (@g_id_log_level = -2147483648))) )
		BEGIN
			IF (1 >= @g_id_log_level)
			BEGIN
				IF (@g_is_print_msg = 1)
				BEGIN
					SELECT @g_tx_tmp_log_msg = '[' + @g_tx_sp_name + ':' + CONVERT(varchar, @g_id_line_num) + '] | ' + CONVERT(varchar, @g_dtt_log) + ' | ' + 'DEBUG' + ' | ' + '?' + ' | [' + CONVERT(varchar, 0) + '] -> ' + ISNULL('USER PREF - UPDATE', '?')
					PRINT  @g_tx_tmp_log_msg
				END
	
				IF ( (@g_is_persist_msg = 1) )
				BEGIN
					EXEC @g_id_return_status = ACT_msg_log   @tx_action_name = 'NEW', @id_env_key = @g_id_env_key, @id_user_mod_key = @id_user_mod_key, @dtt_mod = @g_dtt_log
														   , @id_state_key = @id_state_key, @id_action_key = @id_action_key, @tx_log_msg_type='?', @tx_log_level='DEBUG', @id_log_level=1
														   , @tx_sp_name=@g_tx_sp_name, @id_line_num=@g_id_line_num, @id_err_code=0, @tx_log_msg='USER PREF - UPDATE', @tx_json_log_msg = @tx_json_log_msg OUTPUT
				
					IF (@g_id_return_status != 0)
					BEGIN
						RAISERROR ('Error calling ACT_msg_log', 16, 1)
						IF (@g_is_sp_txn = 1)
						BEGIN
							SELECT @g_is_sp_txn = 0
							ROLLBACK TRANSACTION
					
							IF ( (@@NESTLEVEL = 1) )
							BEGIN
								EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
						
								IF (@g_id_return_status != 0)
								BEGIN
									PRINT 'ERROR SAVING JSON LOG MSG!'
								END
							END
						END
						IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
						BEGIN
							-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
					
							IF (30008 = 0)
							BEGIN
								--RAISERROR (@tx_json_log_msg, 10, 1)
								EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
					
							END
							ELSE
							BEGIN
								--RAISERROR (@tx_json_log_msg, 16, 1)
								EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
							END
						END
					
						RETURN 30008
					END
				END
			END
		END

		UPDATE	T_TMP_PREFERENCE
		SET		  id_pref_key						= PREF.id_pref_key
				, id_pref_ver						= PREF.id_pref_ver
				, tx_type_category					= PREF.tx_type_category
				, tx_type_name						= PREF.tx_type_name
				, id_pref_type_value_key			= PREF.id_pref_type_value_key
				, tx_pref_type_value				= PREF.tx_pref_type_value
				, id_pref_type_value_key_value		= PREF.id_pref_type_value_key_value
				, tx_pref_group						= PREF.tx_pref_group
				, tx_pref_name						= PREF.tx_pref_name
				, tx_pref_value						= PREF.tx_pref_value
				, is_allow_override					= PREF.is_allow_override
				, int_sort_order					= PREF.int_sort_order
				, int_pref_order					= PREF.int_pref_order
				, tx_pref_desc						= PREF.tx_pref_desc

		FROM	V_PREFERENCE		PREF
		JOIN	T_TMP_PREFERENCE	TMP		ON 	TMP.tx_pref_name 		= PREF.tx_pref_name
												AND TMP.tx_pref_group 	= PREF.tx_pref_group
		WHERE	PREF.tx_pref_type_value				= 'USER'
		AND		PREF.id_pref_type_value_key_value	= @id_user_key
		AND		PREF.tx_pref_group					= ISNULL(@tx_pref_group, PREF.tx_pref_group)
		AND		TMP.is_allow_override				!= 0
		AND		PREF.is_active						IN (1, @is_sel_inactive_pref)

		SELECT @g_ct_row = @@rowcount, @g_id_error_key = @@error
		SELECT @g_tx_err_msg = 'Error UPDATING with GROUP preference!'
		IF (@g_id_error_key != 0)
		BEGIN
			RAISERROR (@g_tx_err_msg, 16, 1)
			IF (@g_is_sp_txn = 1)
		BEGIN
			SELECT @g_is_sp_txn = 0
			ROLLBACK TRANSACTION
	
			IF ( (@@NESTLEVEL = 1) )
			BEGIN
				EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
		
				IF (@g_id_return_status != 0)
				BEGIN
					PRINT 'ERROR SAVING JSON LOG MSG!'
				END
			END
		END
			IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
			BEGIN
				-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
		
				IF (99999 = 0)
				BEGIN
					--RAISERROR (@tx_json_log_msg, 10, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
		
				END
				ELSE
				BEGIN
					--RAISERROR (@tx_json_log_msg, 16, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
				END
			END
		
			RETURN 99999
		END

		SELECT @g_tx_log_msg = 'USER PREF UPD COUNT : ' + CONVERT(varchar, @g_ct_row)
		SELECT @g_dtt_log = GETDATE(), @g_tx_sp_name = 'GET_preference.sp', @g_id_line_num = 1450
	
		-- TODO_H : [Naz] Move to Function
	
		IF ( NOT (((@g_id_log_level IS NULL) OR (@g_id_log_level = -2147483648))) )
		BEGIN
			IF (1 >= @g_id_log_level)
			BEGIN
				IF (@g_is_print_msg = 1)
				BEGIN
					SELECT @g_tx_tmp_log_msg = '[' + @g_tx_sp_name + ':' + CONVERT(varchar, @g_id_line_num) + '] | ' + CONVERT(varchar, @g_dtt_log) + ' | ' + 'DEBUG' + ' | ' + '?' + ' | [' + CONVERT(varchar, 0) + '] -> ' + ISNULL(@g_tx_log_msg, '?')
					PRINT  @g_tx_tmp_log_msg
				END
	
				IF ( (@g_is_persist_msg = 1) )
				BEGIN
					EXEC @g_id_return_status = ACT_msg_log   @tx_action_name = 'NEW', @id_env_key = @g_id_env_key, @id_user_mod_key = @id_user_mod_key, @dtt_mod = @g_dtt_log
														   , @id_state_key = @id_state_key, @id_action_key = @id_action_key, @tx_log_msg_type='?', @tx_log_level='DEBUG', @id_log_level=1
														   , @tx_sp_name=@g_tx_sp_name, @id_line_num=@g_id_line_num, @id_err_code=0, @tx_log_msg=@g_tx_log_msg, @tx_json_log_msg = @tx_json_log_msg OUTPUT
				
					IF (@g_id_return_status != 0)
					BEGIN
						RAISERROR ('Error calling ACT_msg_log', 16, 1)
						IF (@g_is_sp_txn = 1)
						BEGIN
							SELECT @g_is_sp_txn = 0
							ROLLBACK TRANSACTION
					
							IF ( (@@NESTLEVEL = 1) )
							BEGIN
								EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
						
								IF (@g_id_return_status != 0)
								BEGIN
									PRINT 'ERROR SAVING JSON LOG MSG!'
								END
							END
						END
						IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
						BEGIN
							-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
					
							IF (30008 = 0)
							BEGIN
								--RAISERROR (@tx_json_log_msg, 10, 1)
								EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
					
							END
							ELSE
							BEGIN
								--RAISERROR (@tx_json_log_msg, 16, 1)
								EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
							END
						END
					
						RETURN 30008
					END
				END
			END
		END


		SELECT @g_dtt_log = GETDATE(), @g_tx_sp_name = 'GET_preference.sp', @g_id_line_num = 1512
	
		-- TODO_H : [Naz] Move to Function
	
		IF ( NOT (((@g_id_log_level IS NULL) OR (@g_id_log_level = -2147483648))) )
		BEGIN
			IF (1 >= @g_id_log_level)
			BEGIN
				IF (@g_is_print_msg = 1)
				BEGIN
					SELECT @g_tx_tmp_log_msg = '[' + @g_tx_sp_name + ':' + CONVERT(varchar, @g_id_line_num) + '] | ' + CONVERT(varchar, @g_dtt_log) + ' | ' + 'DEBUG' + ' | ' + '?' + ' | [' + CONVERT(varchar, 0) + '] -> ' + ISNULL('USER PREF - INSERT', '?')
					PRINT  @g_tx_tmp_log_msg
				END
	
				IF ( (@g_is_persist_msg = 1) )
				BEGIN
					EXEC @g_id_return_status = ACT_msg_log   @tx_action_name = 'NEW', @id_env_key = @g_id_env_key, @id_user_mod_key = @id_user_mod_key, @dtt_mod = @g_dtt_log
														   , @id_state_key = @id_state_key, @id_action_key = @id_action_key, @tx_log_msg_type='?', @tx_log_level='DEBUG', @id_log_level=1
														   , @tx_sp_name=@g_tx_sp_name, @id_line_num=@g_id_line_num, @id_err_code=0, @tx_log_msg='USER PREF - INSERT', @tx_json_log_msg = @tx_json_log_msg OUTPUT
				
					IF (@g_id_return_status != 0)
					BEGIN
						RAISERROR ('Error calling ACT_msg_log', 16, 1)
						IF (@g_is_sp_txn = 1)
						BEGIN
							SELECT @g_is_sp_txn = 0
							ROLLBACK TRANSACTION
					
							IF ( (@@NESTLEVEL = 1) )
							BEGIN
								EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
						
								IF (@g_id_return_status != 0)
								BEGIN
									PRINT 'ERROR SAVING JSON LOG MSG!'
								END
							END
						END
						IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
						BEGIN
							-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
					
							IF (30008 = 0)
							BEGIN
								--RAISERROR (@tx_json_log_msg, 10, 1)
								EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
					
							END
							ELSE
							BEGIN
								--RAISERROR (@tx_json_log_msg, 16, 1)
								EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
							END
						END
					
						RETURN 30008
					END
				END
			END
		END

		INSERT	INTO T_TMP_PREFERENCE
		SELECT	  @id_request_key
				, PREF.id_pref_key
				, PREF.id_pref_ver
				, @id_user_key
				, GETDATE()

				, PREF.tx_type_category
				, PREF.tx_type_name
				, PREF.id_pref_type_value_key
				, PREF.tx_pref_type_value
				, PREF.id_pref_type_value_key_value
				, PREF.tx_pref_group
				, PREF.tx_pref_name
				, PREF.tx_pref_value
				, PREF.is_allow_override
				, PREF.int_sort_order
				, PREF.int_pref_order
				, PREF.tx_pref_desc

		FROM	V_PREFERENCE PREF
		WHERE	PREF.tx_pref_type_value				= 'USER'
		AND		PREF.id_pref_type_value_key_value	= @id_user_key
		AND		PREF.tx_pref_group					= ISNULL(@tx_pref_group, PREF.tx_pref_group)
		AND		PREF.is_active						IN (1, @is_sel_inactive_pref)
		AND		PREF.tx_pref_name					NOT IN ( SELECT tx_pref_name	FROM T_TMP_PREFERENCE WHERE id_request_key = @id_request_key)

		SELECT @g_ct_row = @@rowcount, @g_id_error_key = @@error
		SELECT @g_tx_err_msg = 'Error INSERTING GROUP Preference!'
		IF (@g_id_error_key != 0)
		BEGIN
			RAISERROR (@g_tx_err_msg, 16, 1)
			IF (@g_is_sp_txn = 1)
		BEGIN
			SELECT @g_is_sp_txn = 0
			ROLLBACK TRANSACTION
	
			IF ( (@@NESTLEVEL = 1) )
			BEGIN
				EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
		
				IF (@g_id_return_status != 0)
				BEGIN
					PRINT 'ERROR SAVING JSON LOG MSG!'
				END
			END
		END
			IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
			BEGIN
				-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
		
				IF (99999 = 0)
				BEGIN
					--RAISERROR (@tx_json_log_msg, 10, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
		
				END
				ELSE
				BEGIN
					--RAISERROR (@tx_json_log_msg, 16, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
				END
			END
		
			RETURN 99999
		END

		SELECT @g_tx_log_msg = 'USER PREF INS COUNT : ' + CONVERT(varchar, @g_ct_row)
		SELECT @g_dtt_log = GETDATE(), @g_tx_sp_name = 'GET_preference.sp', @g_id_line_num = 1641
	
		-- TODO_H : [Naz] Move to Function
	
		IF ( NOT (((@g_id_log_level IS NULL) OR (@g_id_log_level = -2147483648))) )
		BEGIN
			IF (1 >= @g_id_log_level)
			BEGIN
				IF (@g_is_print_msg = 1)
				BEGIN
					SELECT @g_tx_tmp_log_msg = '[' + @g_tx_sp_name + ':' + CONVERT(varchar, @g_id_line_num) + '] | ' + CONVERT(varchar, @g_dtt_log) + ' | ' + 'DEBUG' + ' | ' + '?' + ' | [' + CONVERT(varchar, 0) + '] -> ' + ISNULL(@g_tx_log_msg, '?')
					PRINT  @g_tx_tmp_log_msg
				END
	
				IF ( (@g_is_persist_msg = 1) )
				BEGIN
					EXEC @g_id_return_status = ACT_msg_log   @tx_action_name = 'NEW', @id_env_key = @g_id_env_key, @id_user_mod_key = @id_user_mod_key, @dtt_mod = @g_dtt_log
														   , @id_state_key = @id_state_key, @id_action_key = @id_action_key, @tx_log_msg_type='?', @tx_log_level='DEBUG', @id_log_level=1
														   , @tx_sp_name=@g_tx_sp_name, @id_line_num=@g_id_line_num, @id_err_code=0, @tx_log_msg=@g_tx_log_msg, @tx_json_log_msg = @tx_json_log_msg OUTPUT
				
					IF (@g_id_return_status != 0)
					BEGIN
						RAISERROR ('Error calling ACT_msg_log', 16, 1)
						IF (@g_is_sp_txn = 1)
						BEGIN
							SELECT @g_is_sp_txn = 0
							ROLLBACK TRANSACTION
					
							IF ( (@@NESTLEVEL = 1) )
							BEGIN
								EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
						
								IF (@g_id_return_status != 0)
								BEGIN
									PRINT 'ERROR SAVING JSON LOG MSG!'
								END
							END
						END
						IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
						BEGIN
							-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
					
							IF (30008 = 0)
							BEGIN
								--RAISERROR (@tx_json_log_msg, 10, 1)
								EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
					
							END
							ELSE
							BEGIN
								--RAISERROR (@tx_json_log_msg, 16, 1)
								EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
							END
						END
					
						RETURN 30008
					END
				END
			END
		END



	--If we want the result set for example be in GUI then @is_sel_result = 1
	IF (@is_sel_result = 1)
	BEGIN
		SELECT	tx_rs_type = 'RS_TYPE_PREFERENCE', *
		FROM	T_TMP_PREFERENCE
		WHERE	id_request_key = @id_request_key
		ORDER	BY int_sort_order, int_pref_order, tx_pref_type_value, tx_pref_name
	END
	--Else if the proc is called from some other proc to get the preference
	ELSE
	BEGIN
		SELECT 	@tx_pref_value = tx_pref_value
		FROM	T_TMP_PREFERENCE
		WHERE 	id_request_key 	= @id_request_key
		AND 	tx_pref_name 	= ISNULL ( @tx_pref_name , tx_pref_name)
	END

	IF (@is_delete_rec = 1)
	BEGIN
		--Delete all data from Temp table after everything is done
		DELETE FROM T_TMP_PREFERENCE WHERE id_request_key = @id_request_key
	END
END
